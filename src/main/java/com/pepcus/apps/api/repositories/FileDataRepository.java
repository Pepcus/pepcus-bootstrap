package com.pepcus.apps.api.repositories;

import static com.pepcus.apps.api.constant.ApplicationConstants.HR_LENS_NEWSLETTER_KEY;
import static com.pepcus.apps.api.constant.ApplicationConstants.LOCATION_TYPE_COLUMN_VALUE;
import static com.pepcus.apps.api.constant.ApplicationConstants.THINKHR_CRUNCH_NEWSLETTER_KEY;
import static com.pepcus.apps.api.repositories.PrepareStatementBuilder.buildPreparedStatementCreator;
import static com.pepcus.apps.api.repositories.QueryBuilder.INSERT_MARKETOADDS;
import static com.pepcus.apps.api.repositories.QueryBuilder.INSERT_SUBSCRIPTION;
import static com.pepcus.apps.api.repositories.QueryBuilder.buildCompanyInsertQuery;
import static com.pepcus.apps.api.repositories.QueryBuilder.buildLocationInsertQuery;
import static com.pepcus.apps.api.repositories.QueryBuilder.buildQuery;
import static com.pepcus.apps.api.repositories.QueryBuilder.buildUserInsertQuery;
import static com.pepcus.apps.api.repositories.QueryBuilder.defaultCompReqFieldValues;
import static com.pepcus.apps.api.repositories.QueryBuilder.defaultUserReqFieldValues;
import static com.pepcus.apps.api.repositories.QueryBuilder.marketoAddsFields;
import static com.pepcus.apps.api.repositories.QueryBuilder.subscriptionFields;
import static com.pepcus.apps.api.utils.CommonUtil.getTodayInUTC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.pepcus.apps.api.utils.CommonUtil;

import lombok.Data;

@Repository
@Data
public class FileDataRepository {

    @Autowired
    @Qualifier("portalJdbcTemplate")
    JdbcTemplate jdbcTemplate;
    
    /**
     * Saves company & location records in database
     * 
     * @param companyColumns
     * @param companyColumnsValues
     * @param locationColumns
     * @param locationColumnValues
     */

    @Transactional
    public Integer saveCompanyRecord(List<String> companyColumns, List<Object> companyColumnsValues, List<String> locationColumns,
            List<Object> locationColumnValues) {

        String insertClientSql = buildCompanyInsertQuery(companyColumns);

        String insertLocationSql = buildLocationInsertQuery(locationColumns);
        
        KeyHolder keyHolder = new GeneratedKeyHolder();

        companyColumnsValues.addAll(defaultCompReqFieldValues);

        // Saving Company Record
        jdbcTemplate.update(buildPreparedStatementCreator(insertClientSql, companyColumnsValues), keyHolder);

        int clientId = keyHolder.getKey().intValue();
        
        locationColumnValues.add(String.valueOf(clientId));
        locationColumnValues.add(CommonUtil.getTempId());
        locationColumnValues.add(LOCATION_TYPE_COLUMN_VALUE);
        
        // Saving Location Record
        jdbcTemplate.update(buildPreparedStatementCreator(insertLocationSql, locationColumnValues));
        
        return clientId;
    }

    /**
     * Save user's record
     *  
     * @param userColumnsToInsert
     * @param userColumnValues
     */
    public Integer saveUserRecord(List<String> userColumns,
            List<Object> userColumnValues) {

        String insertUserSql = buildUserInsertQuery(userColumns);

        KeyHolder keyHolder = new GeneratedKeyHolder();

        userColumnValues.addAll(defaultUserReqFieldValues);
        jdbcTemplate.update(buildPreparedStatementCreator(insertUserSql, userColumnValues), keyHolder);
        
        return keyHolder.getKey().intValue();
    }

    /**
     * @param subscriptionColumnValues
     * @return
     */
    public Integer saveUserSubscriptionRecord(List<Object> subscriptionColumnValues) {
        
        String insertSubscriptionSql = buildQuery(INSERT_SUBSCRIPTION, subscriptionFields);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(buildPreparedStatementCreator(insertSubscriptionSql, subscriptionColumnValues), keyHolder);
        
        return keyHolder.getKey().intValue();
    }
    
    /**
     * Add user detail in temp_marketoAdds table
     * @param userId
     * @param subscriptionValues
     */
    public void saveUserInMarketo(Integer userId, String[] subscriptionValues, String targetList) {
        Map<String, Object> userDetails = getUserDetailForMarketo(userId);
        List<Object> columnValues = new ArrayList<Object>();
        
        for (Entry<String, Object> entry : userDetails.entrySet()) {
            if (entry.getKey().equals("targetList") && null == entry.getValue()) {
                // Set targetList
                entry.setValue(targetList);
            }
            columnValues.add(entry.getValue());
        }
        
        List<String> columnNames = new ArrayList<String>(marketoAddsFields);
        
        columnNames.add("addDate");
        columnValues.add(getTodayInUTC());
        
        columnNames.add("status");
        columnValues.add("0");
        
        columnNames.add("hasThrPlatform");
        columnValues.add("1");
        
        columnNames.add(HR_LENS_NEWSLETTER_KEY);
        columnValues.add(subscriptionValues[0]);
        columnNames.add(THINKHR_CRUNCH_NEWSLETTER_KEY);
        columnValues.add(subscriptionValues[1]);
        
        String insertMarketoSql = buildQuery(INSERT_MARKETOADDS, columnNames);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(buildPreparedStatementCreator(insertMarketoSql, columnValues), keyHolder);
        
    }

    /**
     * Get user detail to be saved in temp_marketoadds
     * @param userId
     * @return
     */
    public Map<String, Object> getUserDetailForMarketo(Integer userId) {
        
        // PLEASE NOTE: We have directly referred php code for this, 
        // to cross check it please refer method: UserRepository.readAllUsersForMarketo() @line 691
        
        String sql = "SELECT u.contactID, u.first_name, u.last_name, u.phone, u.email, c.client_name, " + 
                "c.clientID as companyId, " + 
                "c.Broker as brokerId, c.Broker as newsletterId, c.Client_Type, b.upsellLearn, " + 
                "(CASE WHEN l.aws_location <> '' THEN l.aws_location ELSE 'https://s3-us-west-2.amazonaws.com/com.thinkhr/public/assets/img/thinkhr.png' END) as logo, " + 
                "    (CASE WHEN pl.access <> '' THEN pl.access ELSE hb.hrh_access END) as hrhAccess, " + 
                "    (CASE WHEN b.display_name <> '' THEN b.display_name ELSE b.client_name END) as brokerName, " + 
                "    (CASE WHEN hb.hotline_phone <> '' THEN hb.hotline_phone ELSE '877.225.1101' END) as hotlinePhoneNumber, " + 
                "    cc1.active as thinkhrLive, cc2.active as thinkhrComply, cc3.active as hasThinkhrLearn, " + 
                "    cc4.active as learnToolBox, " +
                " (CASE WHEN cc5.active != '' THEN cc5.active ELSE 0 END) as hasBenefitsComplianceSuite, " +
                " cc6.active as hasWorkplacePro, cc6.active as hasWorkplaceProNetNew " + 
                "   , CONCAT(e.First_Name, ' ', e.Last_Name) as csmFullName, e.email as csmEmailAddress, " + 
                " (CASE WHEN b.optOut != 1 THEN ml.targetlist ELSE '' END) as targetList, " +
                " (CASE WHEN b.optOutWelcome != 1 THEN ml.welcomelist ELSE '' END) as welcomeList " +
                "    from contacts u  " + 
                "        JOIN clients c ON c.clientId = u.Client_ID " + 
                "        JOIN clients b ON b.clientID = c.Broker " + 
                "    LEFT JOIN app_portal_client_logos l ON l.client_id = b.Broker    " + 
                "    LEFT JOIN profile_live pl ON pl.clientID = b.Broker " + 
                "    LEFT JOIN hrh_brokers hb ON hb.brokerID = b.Broker " + 
                "    LEFT JOIN marketo_lists ml ON ml.brokerID = b.clientID AND ml.broker = CASE WHEN c.clientID = b.clientID THEN 1 ELSE 0 END " +
                "    LEFT JOIN clients_contracts cc1 ON cc1.Client_ID = c.clientID AND cc1.active = 1 AND cc1.Product_ID = 6  " + 
                "    LEFT JOIN clients_contracts cc2 ON cc2.Client_ID = c.clientID AND cc2.active = 1 AND cc2.Product_ID = 22  " + 
                "    LEFT JOIN clients_contracts cc3 ON cc3.Client_ID = c.clientID AND cc3.active = 1 AND cc3.Product_ID = 26  " + 
                "    LEFT JOIN clients_contracts cc4 ON cc4.Client_ID = c.clientID AND cc4.active = 1 AND cc4.Product_ID = 29  " + 
                "    LEFT JOIN clients_contracts cc5 ON cc5.Client_ID = c.clientID AND cc5.active = 1 AND cc5.Product_ID = 32  " + 
                "    LEFT JOIN clients_contracts cc6 ON cc6.Client_ID = c.clientID AND cc6.active = 1 AND cc6.Product_ID = 33 " + 
                "    LEFT JOIN users e ON e.userID = b.partner_manager  " + 
                "    where u.contactID = " + userId + " GROUP BY u.contactID";
        
        return jdbcTemplate.queryForMap(sql);
        
    }

    /**
     * @param companyId
     * @return
     */
    public Map<String, Object> getProductsByCompany(Integer companyId) {
        // TODO: refactoring needed
        String targetListProductIds = "(6, 22, 26)";

        String sql = "SELECT c.clientID, p.productID FROM clients c\r\n" + 
                "            JOIN app_throne_configurations_skus cs ON cs.configurationId = c.t1_configuration_id \r\n" + 
                "            JOIN app_throne_skus s on cs.skuId = s.id\r\n" + 
                "            LEFT JOIN products p ON s.id = p.productID \r\n" + 
                "            where c.clientID = "+companyId+"  and p.productID IN "+targetListProductIds+"" ;
        
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        try {
            map = jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException e ) {
            // do nothing
        }
        
        return map;
    }

    /**
     * @param companyId
     * @return
     */
    public Map<String, Object> getFullCompanyTypeByCompany(Integer companyId) {
        
        String sql = "SELECT c.clientID,\r\n" + 
                "            (CASE WHEN br.Client_Type = 'broker_partner' THEN CONCAT(br.Client_Type, '_',hb.partnerType) ELSE br.Client_Type END) AS clientType,\r\n" + 
                "            (CASE WHEN c.clientID = c.Broker THEN 'broker_contact' ELSE 're_contact' END) AS contactType\r\n" + 
                "                From clients c \r\n" + 
                "                JOIN clients br on br.clientID = c.Broker\r\n" + 
                "                JOIN hrh_brokers hb ON hb.brokerID = br.clientID\r\n" + 
                "                where c.clientID = " + companyId;
        
        Map<String, Object> map = null;
        
        try {
            map = jdbcTemplate.queryForMap(sql);
        } catch (EmptyResultDataAccessException e ) {
            // do nothing
        }
        
        return map;
    }
    
}
