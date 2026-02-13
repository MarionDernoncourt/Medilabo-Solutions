import axios from "axios";

// 1. Définition de l'URL
const API_URL_PATIENT = `${import.meta.env.VITE_API_URL}/api/patient`;

// 2. Création de l'instance (D'un seul bloc !)
const apiClient = axios.create({
  baseURL: API_URL_PATIENT,
  auth: {
    username: import.meta.env.VITE_GATEWAY_USER,
    password: import.meta.env.VITE_GATEWAY_PASSWORD,
  },
});

// 3. Exportation des méthodes
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