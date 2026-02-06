<template>
  <div class="main-container">
    <div class="patient-container">
      <div class="header">
        <h1>📄 Fiche Patient</h1>
        <div class="action-buttons">

          <template v-if="!isEditing">
            <button class="btn-back" @click="goBack">Retour à la liste</button>
            <button class="btn-detail" @click="startEdit">Modifier</button>
          </template>

          <template v-else>
            <button class="btn-save" @click="savePatient">Sauvegarder</button>
            <button class="btn-cancel" @click="stopEdit">Annuler</button>
          </template>
        </div>
      </div>
      <div class="risk-badge-container">
        <span class="risk-label">État de santé :</span>
        <span :class="['risk-badge', getRiskClass(riskLevel)]">
          {{ riskLevel || 'Calcul en cours...' }}
        </span>
      </div>
      <div v-if="message" class="message" :class="{ error: isError }">
        {{ message }}

      </div>

      <div class="details-card">
        <table class="vertical-table">
          <tbody>
            <tr>
              <th>Nom</th>
              <td>
                <div class="input-container">
                  <span v-if="!isEditing">{{ patient.lastname }}</span>
                  <template v-else>
                    <input v-model="editablePatient.lastname" class="edit-input"
                      :class="{ 'input-error': errors.lastname }" />
                    <span v-if="errors.lastname" class="error-text">{{ errors.lastname }}</span>
                  </template>
                </div>
              </td>
            </tr>
            <tr>
              <th>Prénom</th>
              <td>
                <div class="input-container">
                  <span v-if="!isEditing">{{ patient.firstname }}</span>
                  <template v-else>
                    <input v-model="editablePatient.firstname" class="edit-input"
                      :class="{ 'input-error': errors.firstname }" />
                    <span v-if="errors.firstname" class="error-text">{{ errors.firstname }}</span>
                  </template>
                </div>
              </td>
            </tr>
            <tr>
              <th>Date de Naissance</th>
              <td>
                <div class="input-container">
                  <span v-if="!isEditing">{{ patient.birthdate }}</span>
                  <template v-else>
                    <input type="date" v-model="editablePatient.birthdate" class="edit-input"
                      :class="{ 'input-error': errors.birthdate }" />
                    <span v-if="errors.birthdate" class="error-text">{{ errors.birthdate }}</span>
                  </template>
                </div>
              </td>
            </tr>
            <tr>
              <th>Genre</th>
              <td>
                <span v-if="!isEditing"> {{ patient.genre }}</span>
                <template v-else>
                  <select v-model="editablePatient.genre" class="edit-input">
                    <option value="MASCULIN">MASCULIN</option>
                    <option value="FEMININ">FEMININ</option>
                  </select>
                </template>
              </td>
            </tr>
            <tr>
              <th>Adresse</th>
              <td>
                <span v-if="!isEditing"> {{ patient.address }}</span>
                <template v-else>
                  <input v-model="editablePatient.address" class="edit-input"
                    :class="{ 'input-error': errors.address }" />
                  <span v-if="errors.address" class="error-text">{{ errors.address }}</span>
                </template>
              </td>
            </tr>
            <tr>
              <th>Téléphone</th>
              <td>
                <span v-if="!isEditing"> {{ patient.phoneNumber }}</span>
                <template v-else>
                  <input v-model="editablePatient.phoneNumber" class="edit-input"
                    :class="{ 'input-error': errors.phoneNumber }" />
                  <span v-if="errors.phoneNumber" class="error-text">{{ errors.phoneNumber }}</span>
                </template>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    <div class="note-container">
      <div class="header">
        <h1>Notes</h1>
        <div class="action-buttons">
          <button class="btn-addNote" @click="addNote">Ajouter une note</button>
        </div>
      </div>
      <div v-if="noteMessage" class="message" :class="{ error: isError, success: !isError }">
        {{ noteMessage }}

      </div>
      <div class="details-card">



        <table class="vertical-table">
          <thead>
            <tr>
              <th>Date</th>
              <th>Observations</th>
            </tr>
          </thead>
          <tbody>
            <tr v-if="isAddingNote" class="add-note-row">
              <td class="date">Aujourd'hui</td>
              <td>
                <div class="note-edit-wrapper">
                  <textarea v-model="newNoteContent" class="edit-input"></textarea>
                  <div class="note-actions">
                    <button class="btn-save btn-small" @click="saveNote">Enregistrer</button>
                    <button class="btn-cancel btn-small" @click="cancelAddNote">X</button>
                  </div>
                </div>
              </td>
            </tr>

            <tr v-for="note in notes" :key="note.id">
              <td class="date">{{ formatDate(note.createdAt) }}</td>
              <td class="note">{{ note.note }}</td>
            </tr>
            <tr v-if="notes.length === 0">

              <td colspan="2" class="message.error">Aucune note enregistrée</td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
  </div>
</template>

<script setup>

import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from "vue-router";
import PatientService from '../services/PatientService';
import NoteService from '@/services/NoteService';
import ReportService from '@/services/ReportService';

//CONFIGURATION ROUTE
const route = useRoute();
const router = useRouter();
const id = route.params.id;

//ÉTATS DU PATIENT
const patient = ref({});
const editablePatient = ref({});
const isEditing = ref(false);
const message = ref("");
const isError = ref(false);
const errors = ref({});

//ETATS DE RISQUE
const riskLevel = ref("");

//ÉTATS DES NOTES
const notes = ref([]);
const isAddingNote = ref(false);
const newNoteContent = ref("");
const noteMessage = ref("");

//INITIALISATION
onMounted(() => {
  loadPatient();
  loadNotes();
  loadReport();
});

//LOGIQUE PATIENT
const loadPatient = async () => {
  try {
    const response = await PatientService.getPatientById(id);
    patient.value = response.data;
  } catch (error) {
    message.value = "Erreur lors de la récupération du profil";
    isError.value = true;
  }
};

const startEdit = () => {
  editablePatient.value = { ...patient.value };
  errors.value = {};
  message.value = "";
  isEditing.value = true;
};

const stopEdit = () => {
  isEditing.value = false;
  message.value = "";
  errors.value = {};
};

const savePatient = async () => {
  try {
    await PatientService.savePatient(id, editablePatient.value);
    patient.value = { ...editablePatient.value };
    isError.value = false;
    message.value = "Profil mis à jour avec succès";
    stopEdit();
    loadReport();
  } catch (error) {
    isError.value = true;
    if (error.response && error.response.status === 400) {
      message.value = error.response.data.message || "Erreur de validation";
      errors.value = error.response.data.details || {};
    } else {
      message.value = "Erreur lors de la sauvegarde";
    }
  }
};

const goBack = () => {
  router.push({ name: "patients" });
};

// LOGIQUE NOTES
const loadNotes = async () => {
  try {
    const response = await NoteService.getNotesByPatientId(id);
    notes.value = response.data;
  } catch (error) {
    console.error("Erreur chargement notes: ", error);
  }
};

const addNote = () => {
  isAddingNote.value = true;
  newNoteContent.value = "";
  noteMessage.value = "";
};

const cancelAddNote = () => {
  isAddingNote.value = false;
  newNoteContent.value = "";
};

const saveNote = async () => {
  if (!newNoteContent.value) {
    noteMessage.value = "Le message de la note ne peut pas être vide.";
    isError.value = true;
  };

  try {
    const noteData = {
      patientId: id,
      note: newNoteContent.value
    };

    // Appel API
    await NoteService.addNote(noteData);

    // Reset des champs 
    newNoteContent.value = "";
    isAddingNote.value = false;

    // Rafraîchir la liste
    await loadNotes();
    // Rafraichir niveau de risque
    await loadReport();

    isError.value = false; 
    noteMessage.value = "Note ajoutée avec succès !";

    setTimeout(() => { noteMessage.value = ""; }, 1500);

  } catch (error) {
    console.error("Erreur lors de l'ajout de la note:", error);
    noteMessage.value = "Erreur lors de l'enregistrement de la note";
  }
};

// LOGIQUE EVALUATION RISQUE 
const loadReport = async () => {
  try {
    const riskResult = await ReportService.getReportByPatientId(id);
    riskLevel.value = riskResult.data;
  } catch (error) {
    console.error("Erreur lors du chargement du risque", error);
    riskLevel.value = "";
  }
}

//UTILITAIRES

const getRiskClass = (riskLevel) => {
  if (!riskLevel) return '';
  const r = riskLevel.toLowerCase();
  
  if (r.includes('aucun')) return 'risk-none';
  if (r.includes('indéterminé')) return 'risk-na';
  if (r.includes('risque limité')) return 'risk-borderline';
  if (r.includes('en danger')) return 'risk-danger';
  if (r.includes('apparition précoce')) return 'risk-critical';
  
  return '';
};

const formatDate = (dateString) => {
  if (!dateString) return "Date inconnue";
  const date = new Date(dateString);
  return date.toLocaleString("fr-FR", {
    day: "2-digit",
    month: "2-digit",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit"
  });
};
</script>



<style scoped>
.main-container {
  display: flex;
  align-items: flex-start;
  justify-content: space-around;
  width: 100%;
}

.patient-container,
.note-container {
  padding: 20px;
  max-width: 600px;
  margin: 0;
  width: 50%;
}


.main-container .header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.details-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  overflow: hidden;
  border: 1px solid #e0e0e0;
}

.vertical-table {
  width: 100%;
  border-collapse: collapse;
}

.vertical-table th {
  background-color: #f8f9fa;
  color: #34495e;
  padding: 15px;
  width: 40%;
  text-align: left;
  border-bottom: 1px solid #eee;
  border-right: 2px solid #ff6b7f;
  font-weight: 600;
}

.btn-detail,
.btn-back,
.btn-save,
.btn-cancel,
.btn-addNote {
  background-color: #34495e;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-detail:hover,
.btn-back:hover,
.btn-save:hover,
.btn-cancel:hover,
.btn-addNote:hover {
  background-color: #ff6b7f;
}

.edit-input {
  box-sizing: border-box;
  width: 100%;
  padding: 8px;
  border: 1px solid #ff6b7f;
  border-radius: 4px;
  font-size: 1rem;
  color: #2c3e50;
  outline: none;
}

.btn-save,
.btn-back {
  margin-right: 10px;
}

.edit-input:focus {
  box-shadow: 0 0 5px rgba(255, 107, 127, 0.3);
}

.message {
  padding: 10px;
  text-align: center;
  border-radius: 8px;
  margin-bottom: 20px;
  font-weight: bold;
}
.message.error {
  color: #ff6b7f;
  border: 1px solid #ff6b7f;
  background-color: #fff5f6;
}

.message.success {
  color: #27ae60;
  border: 1px solid #27ae60;
  background-color: #ebfaf0;
}

.error-list {
  list-style: none;
  padding: 0;
  margin-top: 5px;
  font-size: 0.9rem;
}

.vertical-table td {
  padding: 15px;
  border-bottom: 1px solid #eee;
  color: #2c3e50;
  font-size: 1.1rem;
  vertical-align: top;
  overflow: hidden;
}

.input-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.error-text {
  color: #ff6b7f;
  font-size: 0.85rem;
  margin-top: 5px;
  font-weight: bold;
  display: block;
}

.input-error {
  border: 2px solid #ff6b7f !important;
  background-color: #fff8f8;
}

tr {
  width: 100%;
}

.note-container .vertical-table th {
  width: 30%;
  border-right: none;
  background-color: #f8f9fa;
  text-align: center;
}

.note-container .vertical-table td.note {
  width: 70%;
  text-align: left;
}

.note-edit-wrapper {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.note-actions {
  display: flex;
  justify-content: flex-end;
  gap: 5px;
}

.btn-small {
  padding: 4px 8px;
  font-size: 0.8rem;
}

.add-note-row {
  background-color: #fff8f9;
}



textarea.edit-input {
  resize: vertical;
  min-height: 60px;
  font-family: inherit;
}

.risk-badge-container {
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.risk-label {
  font-weight: bold;
  color: #34495e;
}

.risk-badge {
  padding: 6px 15px;
  border-radius: 20px;
  font-weight: bold;
  text-transform: uppercase;
  font-size: 1.5rem;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: none;
}

/* Vert - Aucun risque */
.risk-none { background-color: #27ae60; color: white; }

/* Gris/Vert pâle - Non applicable */
.risk-na { background-color: #27ae60; color: white; }

/* Orange - Risque limité */
.risk-borderline { background-color: orange; color: white; }

/* Rouge - Danger */
.risk-danger { background-color: red; color: white; }

/* Violet Foncé ou Bordeaux - Critique (Early Onset) */
.risk-critical { 
  background-color: #8e44ad; /* Violet */
  color: white;
  border: 2px solid #2c3e50; /* Petit contour pour accentuer l'importance */
}
</style>