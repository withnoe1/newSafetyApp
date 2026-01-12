package edu.andrews.safety.controller;

import edu.andrews.safety.model.PermitDetails;
import edu.andrews.safety.service.CitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class CitationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CitationService citationService;

    @GetMapping("/")
    public String index(Model model) {
        // 1. Authorization Check (Keep your existing logic)
        String sqlAuth = "SELECT gwbattr_seqno FROM gwbattr WHERE gwbattr_type = 'TT' AND gwbattr_code = ?";
        List<Integer> authIds = jdbcTemplate.queryForList(sqlAuth, Integer.class, "9999");

        if (authIds.isEmpty()) {
            model.addAttribute("token", "9999");
            return "unauthorized";
        }

        // 2. Load Dropdowns for the Form
        String violationSql = "SELECT gwrviol_code AS code, gwrviol_desc AS description FROM gwrviol ORDER BY gwrviol_desc ASC";
        model.addAttribute("violations", jdbcTemplate.queryForList(violationSql));
        model.addAttribute("locations", fetch("PL"));
        model.addAttribute("states", fetch("ST"));
        model.addAttribute("makes", fetch("MK"));
        model.addAttribute("colors", fetch("CO"));
        model.addAttribute("officers", fetch("OF"));

        model.addAttribute("deviceSeqNo", authIds.get(0));

        return "citation-form"; // This points to templates/citation-form.html
    }

    @GetMapping("/api/permit-lookup")
    @ResponseBody
    public ResponseEntity<PermitDetails> lookup(@RequestParam String number) {
        PermitDetails details = citationService.lookupPermit(number);
        if (details == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(details);
    }

    private List<Map<String, Object>> fetch(String type) {
        return jdbcTemplate.queryForList(
                "SELECT gwbattr_seqno, gwbattr_desc FROM gwbattr WHERE gwbattr_type = ? ORDER BY gwbattr_desc",
                type
        );
    }
}