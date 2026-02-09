import axios from "axios";

// Vers la GATEWAY
const API_URL_REPORT = `${import.meta.env.VITE_API_URL}/api/report`;

const apiReport = axios.create({
    baseURL: API_URL_REPORT,
    //Ajout de l'authentification demandée par la Gateway
    auth: {
        username: import.meta.env.VITE_GATEWAY_USER,
        password: import.meta.env.VITE_GATEWAY_PASSWORD,
    },
});

export default {
    getReportByPatientId(id){
        return apiReport.get("/patient/" + id);
    }
};