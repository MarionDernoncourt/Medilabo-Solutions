<template>
    <div class="container">
        <div class="header">
            <h1>📄 Fiche Patient</h1>
            <div class="action-buttons">
                <button class="btn-back" @click="goBack">Retour à la liste</button>
                <button class="btn-save" @click="save">Sauvegarder</button>
            </div>
        </div>

        <div class="details-card">
            <table class="vertical-table">
                <tbody>
                    <tr>
                        <th>Nom</th>
                        <td>
                            <input v-model="patient.lastname" class="edit-input"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Prénom</th>
                        <td>
                            <input v-model="patient.firstname" class="edit-input"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Date de Naissance</th>
                        <td>
                            <input type="date" v-model="patient.birthdate" class="edit-input"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Genre</th>
                        <td>
                            <select v-model="patient.genre" class="edit-input">
                                <option value="MASCULIN">MASCULIN</option>
                                <option value="FEMININ">FEMININ</option>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <th>Adresse</th>
                        <td>
                            <input v-model="patient.address" class="edit-input"/>
                        </td>
                    </tr>
                    <tr>
                        <th>Téléphone</th>
                        <td>
                            <input v-model="patient.phoneNumber" class="edit-input"/>
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
    try {
        const response = await PatientService.createPatient(patient.value);
        
        const createdPatient = response.data;
        console.log("Patient créé avec l'ID :", createdPatient.id);

        router.push({ 
            name: "patient-detail", 
            params: { id: createdPatient.id } 
        });
    } catch (error) {
        console.error("Erreur lors de la sauvegarde", error);
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

.btn-back,
.btn-save {
  background-color: #34495e;
  color: white;
  border: none;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
}

.btn-back:hover,
.btn-save:hover
 {
  background-color: #ff6b7f;
}

.edit-input {
  width: 100%;
  padding: 8px;
  border: 1px solid #eee;
  border-radius: 4px;
  font-size: 1rem;
  color: #2c3e50;
  outline: none;
}

.btn-save, .btn-back {
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
</style>