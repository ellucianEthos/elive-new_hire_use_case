package com.ellucian;

import com.ellucian.integration.Configuration;
import com.ellucian.integration.auth.SessionToken;
import com.ellucian.integration.proxy.ProxyClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class ProxyRequests {

    public static void main(String[] args) throws IOException {
        SessionToken token = new SessionToken(Configuration.CLIENT_APP_API_KEY);
        ProxyClient client = ProxyClient.getClient(token);

        //build person object
        String first = "Bob";
        String last = "Weir";
        JSONObject person = new JSONObject("{\"id\": \"00000000-0000-0000-0000-000000000000\"}");
        JSONObject name =  new JSONObject((String.format("{\"type\": {\"category\": \"legal\", \"detail\": {\"id\": \"\"}},\"fullName\": \"%s %s\", \"title\": \"\", \"firstName\": \"%s\", \"middleName\": \"\", \"lastName\": \"%s\", \"professionalAbbreviations\": \"\" }",first, last, first, last)));
        person.put("names", new JSONArray().put(name));
        person.put("citizenshipStatus", new JSONObject("{\"category\": \"citizen\", \"detail\": { \"id\": \"\" }}"));
        person.put("countryOfBirth", "USA");
        person.put("citizenshipCountry", "USA");
        person.put("dateOfBirth", "1984-09-01");
        person.put("gender", "male");
        person.put("credentials", new JSONArray().put(new JSONObject("{ \"type\": \"ssn\", \"value\": \"123456789\"}")));
        JSONObject addr = new JSONObject().put("address", new JSONObject().put("addressLines", new JSONArray("[1234 Main St]"))).put("type", new JSONObject("{\"addressType\": \"home\"}"));
        person.put("addresses", new JSONArray().put(addr));

        //post person
        String response = client.post("persons", "application/vnd.hedtech.integration.v12+json", person.toString());
        person = new JSONObject(response);
        String personId = person.getString("id");
        System.out.println("Created new person with id: " + personId);

        //build employee object
        JSONObject employee = new JSONObject("{\"id\":\"00000000-0000-0000-0000-000000000000\",\"homeOrganization\":{\"id\":\"d227c5bd-3a1d-4af5-b5d9-e7bc35764c75\"},\"contract\":{\"detail\":{\"id\":\"\"},\"type\":\"fullTime\"},\"startOn\":\"2010-08-17\",\"benefitStatus\":\"withBenefits\",\"payClass\":{\"id\":\"e271d0a1-f6ff-4c8a-8d93-872c934cd6b2\"},\"payStatus\":\"withPay\",\"status\":\"active\"}");
        employee.put("person", new JSONObject().put("id", personId));
        employee.put("homeOrganization", new JSONObject("{\"id\":\"d227c5bd-3a1d-4af5-b5d9-e7bc35764c75\"}"));
        employee.put("contract", new JSONObject("{\"type\":\"fullTime\",\"detail\":{\"id\":\"\"}}"));
        employee.put("payClass", new JSONObject("{\"id\":\"e271d0a1-f6ff-4c8a-8d93-872c934cd6b2\"}"));
        employee.put("payStatus", "withPay");
        employee.put("benefitStatus", "withBenefits");
        employee.put("status", "active");
        employee.put("startOn", "2010-08-17");

        //post employee
        response = client.post("employees", "application/vnd.hedtech.integration.v12+json", employee.toString());
        employee = new JSONObject(response);
        String employeeId = employee.getString("id");
        System.out.println("Created new employee with id: " + employeeId);

        //build institution-job
        JSONObject job = new JSONObject("{\"id\":\"00000000-0000-0000-0000-000000000000\",\"appointmentPercentage\":100,\"department\":{\"id\":\"d3b8b776-c027-4e0e-ad68-189ef3588bd2\"},\"employer\":{\"id\":\"4ef8d7f5-32ca-4400-afcd-e9d8361e9d93\"},\"fullTimeEquivalent\":1,\"hoursPerPeriod\":[{\"hours\":4,\"period\":\"day\"},{\"hours\":40,\"period\":\"payPeriod\"}],\"payClass\":{\"id\":\"e271d0a1-f6ff-4c8a-8d93-872c934cd6b2\"},\"payCycle\":{\"id\":\"b4872991-a568-469b-9563-ee47fd8efe66\"},\"position\":{\"id\":\"8a89d7f9-c0ff-4422-8168-36b353f91a86\"},\"preference\":\"primary\",\"startOn\":\"2010-09-01\",\"status\":\"active\"}");
        job.put("person", new JSONObject().put("id", personId));

        //post institution-job
        response = client.post("institution-jobs", "application/vnd.hedtech.integration.v12+json", job.toString());
        job = new JSONObject(response);
        System.out.println("Created new job:");
        System.out.println(job.toString(3));
    }

}
