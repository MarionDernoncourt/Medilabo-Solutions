import axios from "axios";

// Vers la GATEWAY
const API_URL_REPORT = "http://localhost:8088/api/report";

const apiReport = axios.create({
    baseURL: API_URL_REPORT,
    //Ajout de l'authentification demandée par la Gateway
    auth: {
        username: "authorized-client",
        password: "SuperSecretPassword2025!",
    },
});

export default {
    getReportByPatientId(id){
        return apiReport.get("/patient/" + id);
    }
};