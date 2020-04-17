package com.pepcus.apps.api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.pepcus.apps.api.db.entities.User;

/**
 * User repository for user entity.
 *  
 */
public interface UserRepository extends BaseRepository<User, Integer> {
    
    public final String isThroneAndActiveUser = "company.isThroneCompany=1 and isActive=1";
    public final String isThroneUser = "company.isThroneCompany=1";
    
    @Query("from User where userId=:id and " + isThroneAndActiveUser )
    public User findActiveOne(@Param("id")Integer id);
    
    /**
     * 
     * @param userID
     */
    @Query("update User user set user.isActive=0 , user.deactivationDate=now(), username = concat(username,'__',:brokerId,'_',user.company.companyId,'_inact'),"
            + " email = concat(email,'__',:brokerId,'_',user.company.companyId,'_inact'), deleted=UNIX_TIMESTAMP() where user.userId = :userId")
    @Modifying
    @Transactional
    public void softDelete(@Param("userId")int userID, @Param("brokerId")int brokerID);
    
    @Query("UPDATE User user set user.isActive=1, user.deactivationDate=null, username = REPLACE(username, CONCAT('__',:brokerId,'_',user.company.companyId,'_inact'), '' ),"
            + "email = REPLACE(email, CONCAT('__',:brokerId,'_',user.company.companyId,'_inact'), '' ),deleted=0 where user.userId = :userId")
    @Modifying
    @Transactional
    public void activate(@Param("userId")int userId, @Param("brokerId")int brokerID);
    
    /**
     * @param userName
     * @return
     */
    @Query("from User where userName=:userName and " + isThroneAndActiveUser)
    public User findByUserName(@Param("userName")String userName);
    
    /**
     * It will get User by userName lookup in all companies not only throne company
     * 
     * @param userName
     * @return
     */
    public User findFirstByUserNameAndIsActive(String userName, Integer isActive);
    
    /**
     * 
     * @param companyId
     * @return
     */
    @Query("from User user where user.company.companyId=:companyId and " + isThroneAndActiveUser)
    public List<User> findByCompanyId(@Param("companyId") Integer companyId);

    /**
     * 
     * @param jobId
     * @return
     */
    @Query(value = "select u.userId from User u where u.searchHelp = :jobId")
    public List<Integer> findAllUsersByJobId(@Param("jobId") String jobId);
    
    
    /**
     * 
     * @param searchHelp
     * @return
     */
    @Query("select u.userId, u.firstName, u.userName, u.email from User u where u.searchHelp=:searchHelp")
    public List<Object[]> findBySearchHelp(@Param("searchHelp")String searchHelp);

    /**
     * 
     * @param jobId
     */
    @Transactional
    public void deleteBySearchHelp(String jobId);
    
    /**
     * 
     * @param companyIdList
     */
    @Transactional
    public void deleteByCompany_companyIdIn(List<Integer> companyIdList);
    
    public List<User> findByCompany_companyIdIn(List<Integer> companyIdList);
    
    @Query("from User where userId=:id and company.isThroneCompany=1 and isActive=:isActive")
    public User findOneWithStatus(@Param("id")Integer id, @Param("isActive")Integer isActive);

    /**
     * Find a duplicate or a potential duplicate i.e. active or inactive name in DB for a user name that is being created or updated
     * 
     * @param username
     * @return
     */
    public List<User> findByUserNameStartingWith(String username);

    /**
     * Get active user by email
     * 
     * @param email
     * @param isActive
     * @return
     */
    public User findFirstByEmailAndIsActive(String email, int isActive);
    
    /**
     * Returns user with specified user Ids
     * 
     * @param userIds
     * @return
     */
    public List<User> findByUserIdIn(List<Integer> userIds);
    
    /**
     * 
     * @param companyId
     * @return
     */
    @Query("select user.userId from User user where user.company.companyId=:companyId and " + isThroneAndActiveUser)
    public List<Integer> findUserIdsByCompanyId(@Param("companyId") Integer companyId);

    /**
     * validate all active userIds for a company
     * 
     * @param parentDocumentId
     * @param userIds
     * @return
     */
    @Query(value="SELECT c.contactID FROM contacts c, app_throne_handbooks h where c.Client_ID = h.companyId and h.id = :parentDocumentId and c.contactID in (:userIds) and c.active = 1",
            nativeQuery=true)
    public List<Integer> findIdsByHandbookIdAndUserIdsIn(@Param("parentDocumentId")Integer parentDocumentId, @Param("userIds")List<Integer> userIds);

}