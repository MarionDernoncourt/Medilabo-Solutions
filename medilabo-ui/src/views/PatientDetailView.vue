<template>
  <div class="container">
    <div class="header">
      <h1>📄 Fiche Patient</h1>
      <button class="btn-back" @click="goBack">⬅ Retour à la liste</button>
    </div>
    
    <div class="details-card">
      <table class="vertical-table">
        <tbody>
          <tr>
            <th>Nom</th>
            <td>{{ patient.lastname }}</td>
          </tr>
          <tr>
            <th>Prénom</th>
            <td>{{ patient.firstname }}</td>
          </tr>
          <tr>
            <th>Date de Naissance</th>
            <td>{{ patient.birthdate }}</td>
          </tr>
          <tr>
            <th>Genre</th>
            <td>{{ patient.genre }}</td>
          </tr>
          <tr>
            <th>Adresse</th>
            <td>{{ patient.address }}</td>
          </tr>
          <tr>
            <th>Téléphone</th>
            <td>{{ patient.phoneNumber }}</td>
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
import PatientsView from './PatientsView.vue';

const route = useRoute();
const router = useRouter();
const patient = ref({});

const id = route.params.id;

const goBack = () => {
    router.push({name: "patients"})
};

onMounted(async () => {
    try {
        const response = await PatientService.getPatientById(id);
        console.log("Données reçus :", response.data);
        patient.value = response.data;
    } catch (error) {
        console.log("Erreur API:", error);
    }
});

</script>


<style scoped>
.container {
  padding: 20px;
  max-width: 600px; /* Plus étroit pour un affichage vertical */
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
  box-shadow: 0 8px 16px rgba(0,0,0,0.1);
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
  width: 40%; /* Colonne de gauche fixe */
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

.btn-back {
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 6px;
  cursor: pointer;
}

.btn-back:hover {
  background-color: #5a6268;
}
</style>