<template>
  <div>
    <h1>👥 Liste des Patients</h1>
    
    <table v-if="patients && patients.length > 0" border="1">
      <thead>
        <tr>
          <th>Nom</th>
          <th>Prénom</th>
          <th>Date de Naissance</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="patient in patients" :key="patient.id">
          <td>{{ patient.lastName }}</td>
          <td>{{ patient.firstName }}</td>
          <td>{{ patient.birthDate }}</td>
        </tr>
      </tbody>
    </table>
    
    <p v-else>Chargement des données ou aucun patient trouvé...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import PatientService from '../services/PatientService';

// On initialise avec un tableau vide pour éviter le "undefined"
const patients = ref([]);

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