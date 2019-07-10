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
    
    public final static String SQL_APP =
            "INSERT INTO app_inf (basic_appid, subscript_status, " + "login_status, language," +
                    "app_install_time, query_num_country_TW,query_type, " + "is_contact, " +
                    "biz_category, is_spam, spam_category) VALUES ('%s', '%s', '%s', " + "'%s'," + "'%s', '%s', '%s', '%s', '%s', '%s','%s')";
    
    public final static String SQL_PERSONAL_ID = "SELECT id FROM personal_inf WHERE uuid = '%s'";
    
    
    //===================  華南 API ======================//
    public final static String SQL_HUANAN_BANK_ACCOUNT = "INSERT INTO bank_account(uuid,birthday,"
            + "gender,career, residence, income, service_units, marital, education,dependents," + "credit_level, is_SNY, is_register_web_bank,is_app_bank,is_register_mobile_pay ) " + "VALUES('%s','%s','%s','%s','%s',%s,'%s','%s','%s',%d,'%s',%d,%d,%d,%d);";
    
    public final static String SQL_BANK_ACCOUNT_ID =
            "SELECT id FROM bank_account WHERE uuid = " + "'%s';";
    
    public final static String SQL_ACCOUNT_NUMBER = "INSERT INTO account_number(user_id," +
            "account_num) VALUES(%d,'%s');";
    
    public final static String SQL_TRANS_RECORD = "INSERT INTO trans_record( user_id, " +
            "account_num,trans_type,trans_channel,trans_date,amount,balance) VALUES" + "(%d,'%s',"
            + "'%s','%s','%s',%d,%d)";
    
    public final static String SQL_LOAN_RECORD = "INSERT INTO loan_record(user_id,amount,percent,"
            + "usage,period,payment_sources,grace_period,property,appraisal,balance,value," +
            "situation,interest_rate) VALUES(%d,%d,%d,'%s',%d,'%s','%s','%s',%d,%d,%d,'%s',%d);";
}
