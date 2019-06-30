package org.iii.modules;

/**
 * Created by Jugo on 2019/6/30
 */

public abstract class SqlHandler
{
    public final static String SQL_PERSONAL = "INSERT INTO personal_inf (uuid, ch_name, " +
            "eng_name, gender,national_id) VALUES ('%s', '%s', '%s', '%s', '%s')";
    
    public final static String SQL_COMMUNICATION = "INSERT INTO communication_inf (mobile, " +
            "telephone, email, address, geographic_location_level, basic_cid) VALUES ('%s'," +
            "'%s', '%s', '%s', '%s', '%s')";
    
    public final static String SQL_JOB = "INSERT INTO job_inf (basic_jid, job_title, occupation, "
            + "customer_seniority, income) VALUES ('%s', '%s', '%s', '%s', '%s')";
    
    public final static String SQL_ACCOUNT = "INSERT INTO account_inf (basic_aid, deposit, " +
            "transfer_remark, " + "atm_times, web_bank_login_times, ip, login_time) VALUES ('%s',"
            + "'%s','%s','%s'," + "'%s','%s','%s')";
    
    public final static String SQL_LOAN = "INSERT INTO loan_inf (basic_lid, " +
            "number_of_other_credit_cards,credit_level, building_type, building_footage, " +
            "audience_category, sources, bad_remark,payment_delay_date) VALUES ('%s','%s', '%s', "
            + "'%s', '%s', '%s','%s', '%s', '%s')";
    
    public final static String SQL_CONSUMPTION = "INSERT INTO consumption_inf (basic_coid, " +
            "shop_name, mcc_code,mcc_type, consumption, credit_transaction_ratio, " +
            "dep_transaction_ratio, latitude_and_longitude) VALUES ('%s', '%s', '%s', '%s', " +
            "'%s', '%s', '%s', '%s')";
    
    public final static String SQL_OTHER_COMMUNICATION =
            "INSERT INTO other_communication_inf " + "(uuid, adid,basic_ocid,region, city, os, " + "os_version, model, manufacturer) VALUES ('%s', '%s', '%s','%s','%s','%s'," + "'%s','%s','%s')";
    
    public final static String SQL_APP = "INSERT INTO app_inf (basic_appid, subscript_status, " +
            "login_status, language," + "app_install_time, query_num_country_TW,query_type, " +
            "is_contact, " + "biz_category, is_spam, spam_category) VALUES ('%s', '%s', '%s', " +
            "'%s'," + "'%s', '%s', '%s', '%s', '%s', '%s','%s')";
    
    public final static String SQL_PERSONAL_ID =
            "SELECT id FROM personal_inf WHERE uuid = '%s'";
}