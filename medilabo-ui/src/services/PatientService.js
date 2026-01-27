import axios from "axios";

// vers la GATEWAY :
const API_URL_PATIENT = "http://localhost:8088/api/patient";

const apiClient = axios.create({
  baseURL: API_URL_PATIENT,
  // Ajout de l'authentification demandée par la GATEWAY
  auth: {
    username: "authorized-client",
    password: "SuperSecretPassword2025!",
  },
});

export default {
  getAllPatients() {
    return apiClient.get("/list");
  },
  getPatientById(id) {
    return apiClient.get("/" + id);
  },
  savePatient(id, editablePatient) {
    return apiClient.put("/" + id, editablePatient);
  },
  createPatient(patient) {
    return apiClient.post("/", patient);
  },
};
