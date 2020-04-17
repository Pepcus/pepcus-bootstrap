package com.pepcus.apps.api.repositories;

import static com.pepcus.apps.api.constant.ApplicationConstants.COMMA_SEPARATOR;
import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_ACTIVE_STATUS;
import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_COLUMN_VALUE;
import static com.pepcus.apps.api.constant.ApplicationConstants.COMPANY_TYPE_CLIENT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.pepcus.apps.api.constant.ApplicationConstants;
import com.pepcus.apps.api.utils.CommonUtil;

/**
 * Query build to build queries
 * 
 */
public class QueryBuilder {

    private static final String INSERT_COMPANY = "INSERT INTO clients";
    private static final String INSERT_LOCATION = "INSERT INTO locations";
    private static final String INSERT_USER = "INSERT INTO contacts";
    public static final String INSERT_SUBSCRIPTION = "INSERT INTO app_throne_subscriptions";
    public static final String INSERT_MARKETOADDS = "INSERT INTO temp_marketoAdds";
    private static final String VALUES = "VALUES";
    private static final String START_BRACES = "(";
    private static final String END_BRACES = ") ";
    public static final String DELETE_COMPANY_QUERY = "DELETE FROM clients WHERE clientId=?";
    public static final String SELECT_PORTAL_COMPANY_QUERY = "SELECT * FROM clients";
    public static final String SELECT_PORTAL_USER_QUERY = "SELECT * FROM contacts";
    public static final String SELECT_LEARN_USER_QUERY = "SELECT * FROM mdl_user";
    public static final String SELECT_LEARN_COMPANY_QUERY = "SELECT * FROM mdl_company";
    public static final String INSERT_PORTAL_COMPANY_CONTRACT = "INSERT INTO clients_contracts ";
    public static final String INSERT_PORTAL_COMPANY_PRODUCT = "INSERT INTO clients_products ";
    public static final String INSERT_LEARN_COMPANY = "INSERT INTO mdl_company ";
    public static final String INSERT_LEARN_PKG_COMPANY = "INSERT INTO mdl_package_company(packageid, companyid) VALUES (?, ?)";
    public static final String SELECT_LEARN_PACAKGE_COMPANY_QUERY = "SELECT * FROM mdl_package_company";
    private static final String INSERT_LEARN_USER = "INSERT INTO mdl_user";
    public static final String INSERT_LEARN_USER_ROLE = "INSERT INTO mdl_role_assignments(roleid,timemodified,contextid,component,modifierid,itemid,sortorder,userid) Values (?,?,?,?,?,?,?,?)";
    
    public static final String UPDATE_USER_DEACTIVATE_QUERY = "UPDATE contacts SET username = concat(username,'__',?,'_',client_id,'_inact'),"
            + "email = concat(email,'__',?,'_',client_id,'_inact'),active=0,deactivationDate=?,deleted=UNIX_TIMESTAMP() where active = 1 and client_id in ";
    
    public static final String UPDATE_COMPANY_DEACTIVATE_QUERY = "UPDATE clients SET client_name = concat(client_name,'__',broker,'_',clientid,'_inact'), "
            + "t1_display_name = concat(t1_display_name,'__',broker,'_',clientid,'_inact'), client_status=0,deactivationDate=?,deactivationID=? where broker = ? and client_status = 1";
    
    private static final String UPDATE_LEARN_COMPANY_DEACTIVATE_QUERY = "UPDATE mdl_company SET suspended=1 where thrclientid in ";
    
    private static final String UPDATE_LEARN_USER_DEACTIVATE_QUERY = "UPDATE mdl_user u INNER JOIN mdl_company c ON u.companyid=c.id SET "
            + "u.username = concat(u.username,'_INACT_',?,'_',c.id),u.email = concat(u.email,'_INACT_',?,'_',c.id), u.suspended = 1, u.deleted=1 "
            + "where u.suspended = 0 and u.companyid in ";
    
    public static final String UPDATE_USER_ACTIVATE_QUERY = "UPDATE contacts SET username = REPLACE(username, CONCAT('__',?,'_',client_id,'_inact'), '' ),"
            + "email = REPLACE(email, CONCAT('__',?,'_',client_id,'_inact'), '' ),active=1,deactivationDate=?,deleted=0 where active = 0 and client_id in ";
    
    public static final String UPDATE_COMPANY_ACTIVATE_QUERY = "UPDATE clients SET client_name = REPLACE(client_name, CONCAT('__',broker,'_',clientid,'_inact'), '' ), "
            + "t1_display_name = REPLACE(t1_display_name, CONCAT('__',broker,'_',clientid,'_inact'), '' ),"
            + "client_status=1,deactivationDate=?,deactivationID=? where broker = ? and client_status = 0";
    
    private static final String UPDATE_LEARN_COMPANY_ACTIVATE_QUERY = "UPDATE mdl_company SET suspended=0 where thrclientid in ";
    
    private static final String UPDATE_LEARN_USER_ACTIVATE_QUERY = "UPDATE mdl_user u INNER JOIN mdl_company c ON u.companyid=c.id SET "
            + "u.username = REPLACE(u.username, concat('_INACT_',?,'_',c.id), '' ),u.email = REPLACE(u.email, concat('_INACT_',?,'_',c.id), '' )"
            + ", u.suspended = 0, u.deleted=0 where u.suspended = 1 and u.companyid in ";
    
    public static final String UPDATE_SINGLE_COMPANY_DEACTIVATE_QUERY = "UPDATE clients SET client_name = concat(client_name,'__',broker,'_',clientid,'_inact'), "
            + "t1_display_name = concat(t1_display_name,'__',broker,'_',clientid,'_inact'), client_status=0,deactivationDate=?,deactivationID=? where clientId = ? and client_status = 1";
    
    public static final String UPDATE_SINGLE_COMPANY_ACTIVATE_QUERY = "UPDATE clients SET client_name = REPLACE(client_name, CONCAT('__',broker,'_',clientid,'_inact'), '' ), "
            + "t1_display_name = REPLACE(t1_display_name, CONCAT('__',broker,'_',clientid,'_inact'), '' ),"
            + "client_status=1,deactivationDate=?,deactivationID=? where clientId = ? and client_status = 0";

    public static List<String> companyRequiredFields;
    public static List<Object> defaultCompReqFieldValues;

    public static List<String> userRequiredFields;
    public static List<Object> defaultUserReqFieldValues;
    public static List<Object> companyContractFieldValues;
    public static List<Object> companyProductFieldValues;
    public static List<String> learnCompanyFields;
    public static List<String> companyContractFields;
    public static List<String> companyProductFields;
    public static List<String> locationRequiredFields;
    public static List<String> subscriptionFields;
    public static List<String> marketoAddsFields;
    static {
        companyRequiredFields = new ArrayList<String>(Arrays.asList(
                                                                    "client_type", 
                                                                    "special_note", 
                                                                    "client_since", 
                                                                    "t1_is_active",
                                                                    "tempID"));
        
        locationRequiredFields = new ArrayList<String>(Arrays.asList("client_id",
                                                                     "tempID",
                                                                     "Location_Type"));

        defaultCompReqFieldValues = new ArrayList<Object>(Arrays.asList(
                                                                        COMPANY_TYPE_CLIENT, 
                                                                        DEFAULT_COLUMN_VALUE, 
                                                                        CommonUtil.getTodayInUTC(), 
                                                                        DEFAULT_ACTIVE_STATUS, //default all clients are active
                                                                        CommonUtil.getTempId())); 

        userRequiredFields = new ArrayList<String>(Arrays.asList( 
                                                                 "mkdate", 
                                                                 "codevalid", 
                                                                 "update_password", 
                                                                 "blockedaccount"));

        defaultUserReqFieldValues =  new ArrayList<Object>(Arrays.asList( 
                                                                         DEFAULT_COLUMN_VALUE, 
                                                                         DEFAULT_COLUMN_VALUE, 
                                                                         DEFAULT_COLUMN_VALUE, 
                                                                         new Integer(0)));
        
        learnCompanyFields = new ArrayList<String>(Arrays.asList("thrclientid", 
                                                                 "company_name", 
                                                                 "company_type",
                                                                 "company_key", 
                                                                 "address", 
                                                                 "address2", 
                                                                 "city", 
                                                                 "state", 
                                                                 "zip",
                                                                 "partnerid", 
                                                                 "phone", 
                                                                 "createdby", 
                                                                 "timecreated"));
        
        subscriptionFields =  new ArrayList<String>(Arrays.asList("userId", 
                                                                    "newsletterId", 
                                                                    "active", 
                                                                    "createdOnDate"));
        
        marketoAddsFields = new ArrayList<String>(Arrays.asList("contactID",
                                                                "First_Name",
                                                                "Last_Name",
                                                                "Phone",
                                                                "Email",
                                                                "Company",
                                                                "clientID",
                                                                "brokerID",
                                                                "newsletterID",
                                                                "clientType",
                                                                "Can_sell_Learn_Admin_Toolbox_c",
                                                                "logo",
                                                                "hrh_access",
                                                                "brokerName",
                                                                "hotlinePhoneNumber",
                                                                "ThinkHRLive",
                                                                "ThinkHRComply",
                                                                "hasThinkHRLearn",
                                                                "Learntoolbox",
                                                                "HasBenefitsComplianceSuite",
                                                                "HasWorkplacePro",
                                                                "workplaceProNetNew",
                                                                "cSMFullname",
                                                                "cSMEmailaddress",
                                                                "targetlist",
                                                                "welcomelist"));
    
    }
    /**
     *   //INSERT INTO locations(address,address2,city,state,zip,client_id) values(?,?,?,?,?,?);
     * 
     * @param locationColumns
     * @return
     */
    public static String buildLocationInsertQuery(List<String> locationColumns) {
        locationColumns.addAll(locationRequiredFields);
        return buildQuery(INSERT_LOCATION, locationColumns);
    }

    /**
     * Build query
     * 
     * @param insertQueryType
     * @param columns
     * @return
     */
    public static String buildQuery(String insertQueryType, List<String> columns) {
        StringBuffer insertSql = new StringBuffer();
        insertSql.append(insertQueryType)
                .append(START_BRACES).append(StringUtils.join(columns, COMMA_SEPARATOR))
        .append(END_BRACES)
        .append(VALUES)
                .append(START_BRACES).append(getQueryParaSpecifiers(columns.size()))
        .append(END_BRACES);
        return insertSql.toString();
    }

    /**
     *    INSERT INTO clients(client_name,display_name, client_phone,industry,companySize,producer,custom1,custom2,custom3,custom4,
     *    search_help,client_type,client_since,special_note) " + "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)
     *
     * @param companyColumns
     * @return
     */
    public static String buildCompanyInsertQuery(List<String> companyColumns) {
        companyColumns.addAll(companyRequiredFields);
        return buildQuery(INSERT_COMPANY, companyColumns);
    }
    
    /**
     * Generate string having query parameters for given count
     * 
     * @param locationColumns
     * @return
     */
    public static String getQueryParaSpecifiers(Integer count) {
        if (count == 0) {
            return "";
        }
        StringBuffer params = new StringBuffer();
        for (int i = 0; i < count; i++) {
            if ( i > 0) {
                params.append(ApplicationConstants.COMMA_SEPARATOR);
            }
            params.append(ApplicationConstants.QUERY_SEPARATOR);
        }
        
        return params.toString();
    }
   
    
    /**
     * @param userColumns
     * @return
     */
    public static String buildUserInsertQuery(List<String> userColumns) {
        userColumns.addAll(userRequiredFields);
        return buildQuery(INSERT_USER, userColumns);
    }
    
    /**
     * Builds insert query for inserting records into mdl_user table for given list of columns
     * 
     * @param userColumns
     * @return
     */
    public static String buildLearnUserInsertQuery(List<String> userColumns) {
        return buildQuery(INSERT_LEARN_USER, userColumns);
    }

    /**
     * Activate or Deactivate Learn User
     * 
     * @param activeStatus
     * @param numberOfRecordsInClause
     * @return
     */
    public static String getQueryToActivateOrDeactivateLearnUser(Integer activeStatus, int numberOfRecordsInClause) {
        StringBuffer query = new StringBuffer();
        return query.append(activeStatus.equals(0) ? UPDATE_LEARN_USER_DEACTIVATE_QUERY : UPDATE_LEARN_USER_ACTIVATE_QUERY)
                .append(START_BRACES).append(getQueryParaSpecifiers(numberOfRecordsInClause)).append(END_BRACES).toString();
    }

    /**
     * Activate or Deactivate Learn Company
     * 
     * @param activeStatus
     * @param numberOfRecordsInClause
     * @return
     */
    public static String getQueryToActivateOrDeactivateLearnCompany(Integer activeStatus, int numberOfRecordsInClause) {
        StringBuffer query = new StringBuffer();
        return query.append(activeStatus.equals(0) ? UPDATE_LEARN_COMPANY_DEACTIVATE_QUERY : UPDATE_LEARN_COMPANY_ACTIVATE_QUERY)
                .append(START_BRACES).append(getQueryParaSpecifiers(numberOfRecordsInClause)).append(END_BRACES).toString();
    }

    /**
     * Activate or Deactivate Throne Company
     * 
     * @param activeStatus
     * @return
     */
    public static String getQueryToActivateOrDeactivateThroneCompany(Integer activeStatus) {
        return activeStatus.equals(0) ? UPDATE_COMPANY_DEACTIVATE_QUERY : UPDATE_COMPANY_ACTIVATE_QUERY;
    }
    
    /**
     * Activate or Deactivate Single Throne Company
     * 
     * @param activeStatus
     * @return
     */
    public static String getQueryToActivateOrDeactivateSingleThroneCompany(Integer activeStatus) {
        return activeStatus.equals(0) ? UPDATE_SINGLE_COMPANY_DEACTIVATE_QUERY : UPDATE_SINGLE_COMPANY_ACTIVATE_QUERY;
    }
    
    /**
     * Activate or Deactivate Throne User
     * 
     * @param activeStatus
     * @param numberOfRecordsInClause
     * @return
     */
    public static String getQueryToActivateOrDeactivateThroneUser(Integer activeStatus, int numberOfRecordsInClause) {
        StringBuffer query = new StringBuffer();
        return query.append(activeStatus.equals(0) ? UPDATE_USER_DEACTIVATE_QUERY : UPDATE_USER_ACTIVATE_QUERY)
                .append(START_BRACES).append(getQueryParaSpecifiers(numberOfRecordsInClause)).append(END_BRACES).toString();
    }
}
