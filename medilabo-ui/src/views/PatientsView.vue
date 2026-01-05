<template>
  <div class="container">
    <h1>Liste des Patients</h1>
    
    <div class="table-container">
      <table v-if="patients && patients.length > 0" class="patient-table">
        <thead>
          <tr>
            <th>Nom</th>
            <th>Prénom</th>
            <th>Date de Naissance</th>
            <th>Genre</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="patient in patients" :key="patient.id">
            <td><strong>{{ patient.lastname }}</strong></td>
            <td>{{ patient.firstname }}</td>
            <td>{{ patient.birthdate }}</td>
            <td>{{ patient.genre }}</td>
            <td>
              <button class="btn-detail" @click="goToPatient(patient.id)">Détails</button>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-else class="loading">Chargement des données...</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import PatientService from '../services/PatientService';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';

// On initialise avec un tableau vide pour éviter le "undefined"
const patients = ref([]);

const router = useRouter();
const route = useRoute();


const goToPatient = (id) => {
  router.push({name: "patient-detail", params: { id : id}});
};

onMounted(async () => {
  try {
    const response = await PatientService.getAllPatients();
    console.log("Données reçues :", response.data);
    patients.value = response.data;
  } catch (error) {
    console.error("Erreur API :", error);
    // On ne bloque pas l'affichage, on laisse le tableau vide
  }
});
</script>

<style scoped>
.container {
  padding: 20px;
  max-width: 1000px;
  margin: 0 auto;
  font-family: Arial, sans-serif;
}

h1 {
  color: #2c3e50;
  margin-bottom: 20px;
}

.table-container {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.patient-table {
  width: 100%;
  border-collapse: collapse;
  text-align: left;
}

.patient-table th {
  background-color: #ff6b7f;
  color: white;
  padding: 15px;
  text-transform: uppercase;
  font-size: 0.9rem;
}

.patient-table td {
  padding: 12px 15px;
  border-bottom: 1px solid #eee;
}

.patient-table tr:hover {
  background-color: #f9f9f9;
}

.btn-detail {
  background-color: #34495e;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-detail:hover {
  background-color: #ff6b7f;
}

.loading {
  padding: 40px;
  text-align: center;
  color: #666;
}
</style>