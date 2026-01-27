import axios from "axios";

// vers la GATEWAY :
const API_URL_NOTE = "http://localhost:8088/api/notes";

const apiNote = axios.create({
    baseURL: API_URL_NOTE,
     // Ajout de l'authentification demandée par la GATEWAY
    auth: {
        username: "authorized-client",
        password: "SuperSecretPassword2025!"
    }
})

export default {
    getNotesByPatientId(id) {
        return apiNote.get("/patient/" + id);
    },
    addNote(note){ 
        return apiNote.post("/add", note)
    }
}