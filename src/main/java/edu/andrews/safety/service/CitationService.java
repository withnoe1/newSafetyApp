package edu.andrews.safety.service;

import edu.andrews.safety.model.PermitDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CitationService {

    @Autowired
    private JdbcTemplate mySqlJdbcTemplate;

    @Autowired
    @Qualifier("oracleJdbcTemplate")
    private JdbcTemplate oracleJdbcTemplate;

    public PermitDetails lookupPermit(String permitCode) {
        PermitDetails details = new PermitDetails();

        // 1. GET MYSQL DATA
        try {
            // Changed status back to 'A' (Active). If you want both, use IN ('A', 'I')
            String mysqlSql = "SELECT gwrpapp_pidm, gwrpapp_vlicense, gwrpapp_vin, gwrpapp_vstate_code, " +
                    "gwrpapp_vmake_code, gwrpapp_vcolr_code " +
                    "FROM gwrpapp WHERE gwrpapp_permit_code = ? AND gwrpapp_permit_status = 'I'";

            List<Map<String, Object>> mysqlRows = mySqlJdbcTemplate.queryForList(mysqlSql, permitCode);

            if (mysqlRows.isEmpty()) return null;

            Map<String, Object> mySqlRes = mysqlRows.get(0);
            details.setPlate((String) mySqlRes.get("gwrpapp_vlicense"));
            details.setVin((String) mySqlRes.get("gwrpapp_vin"));
            details.setState(String.valueOf(mySqlRes.get("gwrpapp_vstate_code")));
            details.setMake(String.valueOf(mySqlRes.get("gwrpapp_vmake_code")));
            details.setColor(String.valueOf(mySqlRes.get("gwrpapp_vcolr_code")));

            // Extract PIDM for the next query
            Object pidm = mySqlRes.get("gwrpapp_pidm");

            // 2. ATTEMPT ORACLE
            if (pidm != null) {
                try {
                    String oracleSql = "SELECT spriden_id as id, " +
                            "spriden_first_name || ' ' || spriden_last_name as name, " +
                            "'('||sprtele_phone_area||')' || SUBSTR(sprtele_phone_number, 1, 3) || '-' || SUBSTR(sprtele_phone_number, 4) as workphone, " +
                            "spraddr_street_line1 as depart, " +
                            "spraddr_street_line2 as title, " +
                            "spraddr_street_line3 as location " +
                            "FROM spriden " +
                            "LEFT JOIN spraddr ON spriden_pidm = spraddr_pidm AND spraddr_atyp_code = 'WK' AND spraddr_to_date IS NULL " +
                            "LEFT JOIN sprtele ON spriden_pidm = sprtele_pidm AND sprtele_atyp_code = 'WK' AND sprtele_status_ind IS NULL " +
                            "WHERE spriden_pidm = ? AND spriden_change_ind IS NULL";

                    List<Map<String, Object>> oraRows = oracleJdbcTemplate.queryForList(oracleSql, pidm);

                    if (!oraRows.isEmpty()) {
                        Map<String, Object> oraRes = oraRows.get(0);
                        // ALL KEYS MUST BE UPPERCASE FOR ORACLE MAPS
                        details.setId((String) oraRes.get("ID"));
                        details.setFullName((String) oraRes.get("NAME"));
                        details.setWorkPhone((String) oraRes.get("WORKPHONE"));
                        details.setDepart((String) oraRes.get("DEPART"));   // Changed to uppercase
                        details.setTitle((String) oraRes.get("TITLE"));     // Changed to uppercase
                        details.setLocation((String) oraRes.get("LOCATION")); // Changed to uppercase
                    }
                } catch (Exception e) {
                    System.err.println("Oracle failed: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("MySQL Error: " + e.getMessage());
            return null;
        }

        return details;
    }
}