import axios from "axios";

// vers la GATEWAY :
const API_URL_NOTE = `${import.meta.env.VITE_API_URL}/api/notes`;

const apiNote = axios.create({
    baseURL: API_URL_NOTE,
     // Ajout de l'authentification demandée par la GATEWAY
    auth: {
        username: import.meta.env.VITE_GATEWAY_USER,
        password: import.meta.env.VITE_GATEWAY_PASSWORD,
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