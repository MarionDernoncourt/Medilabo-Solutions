import axios from "axios";

// vers la GATEWAY :
const API_URL = "http://localhost:8088/api/patient";

const apiClient = axios.create({
    baseURL: API_URL,
    // Ajout de l'authentification demandée par la GATEWAY
    auth: {
        username: "web-client-app",
        password: "SuperSecretPassword2025!"
    }
});

export default {
    getAllPatients() {
        return apiClient.get("/all");
    }
}