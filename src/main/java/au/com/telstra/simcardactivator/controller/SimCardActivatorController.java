package au.com.telstra.simcardactivator.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/sim")
public class SimCardActivatorController {

    @Value("${actuator.url:http://localhost:8444/actuate}")
    private String actuatorUrl;

    @PostMapping("/activate")
    public ResponseEntity<String> activateSim(@RequestBody SimActivationRequest request) {
        try {
            // Create payload for actuator
            ActuatorRequest actuatorRequest = new ActuatorRequest(request.getIccid());

            // Make POST request to actuator
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(actuatorUrl, actuatorRequest, ActuatorResponse.class);

            if (response.getBody() != null && response.getBody().isSuccess()) {
                return ResponseEntity.ok("SIM activation successful!");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SIM activation failed.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during SIM activation: " + e.getMessage());
        }
    }

    // Inner class for incoming request payload
    static class SimActivationRequest {
        private String iccid;
        private String customerEmail;

        // Getters and setters
        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }
    }

    // Inner class for actuator request payload
    static class ActuatorRequest {
        private String iccid;

        public ActuatorRequest(String iccid) {
            this.iccid = iccid;
        }

        // Getter and setter
        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }
    }

    // Inner class for actuator response payload
    static class ActuatorResponse {
        private boolean success;

        // Getter and setter
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }
}
