<template>
  <div class="container">
    <div class="header">
      <h1>📄 Nouveau Patient</h1>
      <div class="action-buttons">
        <button class="btn-back" @click="goBack">Retour à la liste</button>
        <button class="btn-save" @click="save">Sauvegarder</button>
      </div>
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
                <input v-model="patient.lastname" class="edit-input" :class="{ 'input-error': errors.lastname }" />
                <span v-if="errors.lastname" class="error-text">{{ errors.lastname }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>Prénom</th>
            <td>
              <div class="input-container">
                <input v-model="patient.firstname" class="edit-input" :class="{ 'input-error': errors.firstname }" />
                <span v-if="errors.firstname" class="error-text">{{ errors.firstname }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>Date de Naissance</th>
            <td>
              <div class="input-container">
                <input type="date" v-model="patient.birthdate" class="edit-input" :class="{ 'input-error': errors.birthdate }" />
                <span v-if="errors.birthdate" class="error-text">{{ errors.birthdate }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>Genre</th>
            <td>
              <div class="input-container">
                <select v-model="patient.genre" class="edit-input" :class="{ 'input-error': errors.genre }">
                  <option value="MASCULIN">MASCULIN</option>
                  <option value="FEMININ">FEMININ</option>
                </select>
                <span v-if="errors.genre" class="error-text">{{ errors.genre }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>Adresse</th>
            <td>
              <div class="input-container">
                <input v-model="patient.address" class="edit-input" :class="{ 'input-error': errors.address }" />
                <span v-if="errors.address" class="error-text">{{ errors.address }}</span>
              </div>
            </td>
          </tr>
          <tr>
            <th>Téléphone</th>
            <td>
              <div class="input-container">
                <input v-model="patient.phoneNumber" class="edit-input" :class="{ 'input-error': errors.phoneNumber }" />
                <span v-if="errors.phoneNumber" class="error-text">{{ errors.phoneNumber }}</span>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import PatientService from '@/services/PatientService';
import { useRouter } from 'vue-router';

const router = useRouter();

// Variables pour la gestion des messages et erreurs
const message = ref("");
const isError = ref(false);
const errors = ref({}); // Initialisation correcte avec ref

const patient = ref({
  lastname: '',
  firstname: '',
  birthdate: '',
  genre: 'MASCULIN',
  address: '',
  phoneNumber: ''
});

const goBack = () => {
  router.push({ name: "patients" })
};

const save = async () => {
  // Réinitialisation avant chaque tentative
  message.value = "";
  isError.value = false;
  errors.value = {};

  try {
    const response = await PatientService.createPatient(patient.value);
    const createdPatient = response.data;
    
    isError.value = false;
    message.value = "Patient créé avec succès !";

    // Redirection vers le détail après un court délai
    setTimeout(() => {
        router.push({
          name: "patient-detail",
          params: { id: createdPatient.id }
        });
    }, 1000);

  } catch (error) {
    isError.value = true;
    if (error.response && error.response.status === 400) {
      // On récupère le message global et le dictionnaire d'erreurs
      message.value = error.response.data.message || "Erreur de validation";
      errors.value = error.response.data.details || {};
    } else {
      message.value = "Erreur de connexion au serveur";
      console.error("Erreur technique :", error);
    }
  }
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}

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
  vertical-align: middle;
}

.edit-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  color: #2c3e50;
  outline: none;
}

/* BOUTONS */
.btn-back, .btn-save {
  background-color: #34495e;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-back:hover, .btn-save:hover {
  background-color: #ff6b7f;
}

.btn-save { margin-left: 10px; }

/* MESSAGE GLOBAL */
.message {
  padding: 12px;
  margin-bottom: 20px;
  border-radius: 8px;
  background-color: #e8f5e9;
  color: #2e7d32;
  border: 1px solid #2e7d32;
  text-align: center;
  font-weight: bold;
}

.message.error {
  background-color: #fff5f6;
  color: #ff6b7f;
  border: 1px solid #ff6b7f;
}
.input-error {
  border: 2px solid #ff6b7f !important; 
  background-color: #fff8f8;
}

.error-text {
  color: #ff6b7f;
  font-size: 0.8rem;
  font-weight: bold;
  margin-top: 4px;
  display: block; 
}

.input-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  justify-content: center;
}
</style>