<template>
  <div class="container">
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

    <div v-if="message" class="message" :class="{ error: isError }">
      {{ message }}
      <ul v-if="Object.keys(errors).length > 0" class="error-list">
        <li v-for="(msg, field) in errors" :key="field">{{ msg }}</li>
      </ul>
    </div>

    <div class="details-card">
      <table class="vertical-table">
        <tbody>
          <tr>
            <th>Nom</th>
            <td>
              <span v-if="!isEditing">{{ patient.lastname }}</span>
              <input v-else v-model="editablePatient.lastname" class="edit-input" />
            </td>
          </tr>
          <tr>
            <th>Prénom</th>
            <td>
              <span v-if="!isEditing">{{ patient.firstname }}</span>
              <input v-else v-model="editablePatient.firstname" class="edit-input" />
            </td>
          </tr>
          <tr>
            <th>Date de Naissance</th>
            <td>
              <span v-if="!isEditing">{{ patient.birthdate }}</span>
              <input v-else type="date" v-model="editablePatient.birthdate" class="edit-input" />
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
              <input v-else v-model="editablePatient.address" class="edit-input" />
            </td>
          </tr>
          <tr>
            <th>Téléphone</th>
            <td>
              <span v-if="!isEditing"> {{ patient.phoneNumber }}</span>
              <input v-else v-model="editablePatient.phoneNumber" class="edit-input" />
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>

import { ref, onMounted } from 'vue';
import PatientService from '../services/PatientService';
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const patient = ref({});
const id = route.params.id;

const message = ref("");
const isError = ref(false);
const errors = ref({});

const isEditing = ref(false);
const editablePatient = ref({});

const goBack = () => {
  router.push({ name: "patients" })
};

onMounted(async () => {
  try {
    const response = await PatientService.getPatientById(id);
    patient.value = response.data;
  } catch (error) {
    message.value = "Erreur lors de la récupération du profil";
    isError.value = true;
  }
});


//Basculer en mode edition
const startEdit = () => {
  editablePatient.value = { ...patient.value };
  errors.value = {};
  message.value = "";
  isEditing.value = true;
};

//Annuler edition
const stopEdit = () => {
  isEditing.value = false;
  message.value = "";
  errors.value = {};
};

//Sauvegarder edition
const savePatient = async () => {
  try {
    await PatientService.savePatient(id, editablePatient.value);
    patient.value = { ...editablePatient.value };
    isError.value = false;
    message.value = "Profil mis à jour avec succès";
    setTimeout(() => {
      stopEdit();
    }, 1000);

  } catch (error) {
    isError.value = true;
    if (error.response && error.response.status === 400) {
      message.value = error.response.data.message || "Erreur de validation";
      errors.value = error.response.data.details || {};
    } else {
      message.value = "Erreur lors de la sauvegarde";
    } 
    console.error("Detail de l'erreur: ", error);
  }
};


</script>


<style scoped>
.container {
  padding: 20px;
  max-width: 600px;
  margin: 0 auto;
}

.header {
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

.vertical-table td {
  padding: 15px;
  border-bottom: 1px solid #eee;
  color: #2c3e50;
  font-size: 1.1rem;
}

.btn-detail,
.btn-back,
.btn-save,
.btn-cancel {
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
.btn-cancel:hover {
  background-color: #ff6b7f;
}

.edit-input {
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

td {
  display: flex;
  align-items: center;
  min-height: 50px;
}

.message {
  padding: 15px;
  margin-bottom: 20px;
  border-radius: 8px;
  background-color: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #2e7d32;
  text-align: center;
}

.message.error {
  background-color: #fff5f6;
  color: #ff6b7f;
  border: 1px solid #ff6b7f;
}

.error-list {
  list-style: none;
  padding: 0;
  margin-top: 5px;
  font-size: 0.9rem;
}

.input-error {
  border: 2px solid #ff6b7f !important;
  background-color: #fff8f8;
}
</style>