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
    public final static String SQL_HUANAN_BANK_ACCOUNT = "INSERT INTO bank_account(uuid," +
            "serial,identity_id,license_no,birthday," + "gender,career, residence, income, " +
            "service_units, " +
            "marital, " +
            "education,dependents," + "credit_level, is_SNY, is_register_web_bank,is_app_bank," + "is_register_mobile_pay ) " + "VALUES('%s',%d,'%s','%s','%s','%s','%s','%s',%s,'%s','%s','%s'," + "%d,'%s',%d,%d,%d,%d);";
    
    public final static String SQL_BANK_ACCOUNT_ID =
            "SELECT id FROM bank_account WHERE uuid = " + "'%s';";
    
    public final static String SQL_ACCOUNT_NUMBER = "INSERT INTO account_number(user_id," +
            "account_num) VALUES(%d,'%s');";
    
    public final static String SQL_TRANS_RECORD = "INSERT INTO trans_record( user_id, " +
            "account_num,trans_type,trans_channel,trans_date,amount,balance) VALUES" + "(%d,'%s',"
            + "'%s','%s','%s',%d,%d)";
    
    public final static String SQL_LOAN_RECORD = "INSERT INTO loan_record(user_id,amount,percent,"
            + "usage,period,payment_sources,grace_period,property,appraisal,balance,value," +
            "situation,interest_rate) VALUES(%d,%d,%d,'%s',%d,'%s','%s','%s',%d,%d,%d,'%s',%f);";
    
    public final static String SQL_CONSTRUCTION_RECORD = "INSERT INTO construction_record" +
            "(user_id,benefit_id,property,location,building_type,proximity_attr,house_age) " +
            "VALUES" + "(%d,'%s','%s','%s','%s','%s','%s')";
    
    public final static String SQL_FUND_INFORMATION = "INSERT INTO fund_information(user_id," +
            "fund_code,fund_name,price_currency,dividend_category,net_datetime,net ) VALUES(%d," + "'%s','%s','%s','%s','%s',%f)";
    
    public final static String SQL_FUND_ACCOUNT = "INSERT INTO fund_account( user_id," +
            "account_category,capital,bank_code,account_number )VALUES(%d,'%s',%d,'%s','%s')";
    
    public final static String SQL_FUND_INVENTORY = "INSERT INTO fund_inventory(user_id," +
            "fund_code, fund_name,price_currency,inventory_unit,investment_cost)VALUES(%d,'%s'," + "'%s','%s',%d,%d)";
    
    public final static String SQL_BENEFICIARY = "INSERT INTO beneficiary(user_id,benefit_id," +
            "benefit_name,birth,risk,risk_exp_date )VALUES(%d,'%s','%s','%s',%d,'%s')";
    
    public final static String SQL_BLACK_LIST =
            "INSERT INTO blacklist(identity_id, license_no)" + "VALUES('%s','%s')";
    
    public final static String SQL_TOKENS = "INSERT INTO tokens(token)VALUES('%s')";
    
    public final static String SQL_CLAM_RECORD =
            "INSERT INTO claim_record( claim_no,policy_no," + "claim_date,claim_amount," +
                    "claim_descript )VALUES('%s','%s','%s',%d,'%s')";
    
    public final static String SQL_INSURANCE_EXP_DATE = "INSERT INTO insurance_exp_date" +
            "(identity_id,policy_no,exp_date,is_active )VALUES('%s','%s','%s',%d)";
    
    public final static String SQL_INSURANCE_RECORD = "INSERT INTO insurance_record(user_id," +
            "policy_no,classification,insurance_category,insurance_name,insurance_premiums," +
            "insurance_date,insurance_expiration_date,car_type )VALUES(%d,'%s','%s','%s','%s',%d,"
            + "'%s','%s','%s')";
    
    public final static String SQL_ONLINE_REPORT = "INSERT INTO online_report(policy_no," +
            "identity_id,license_no,claim_date )VALUES('%s','%s','%s','%s')";
    
    public final static String SQL_PAYMENT_METHOD = "INSERT INTO payment_method(user_id," +
            "policy_no,pay_status,pay_date,pay_category,payments_period )VALUES(%d,'%s',%d,'%s'," +
            "'%s','%s')";
}
