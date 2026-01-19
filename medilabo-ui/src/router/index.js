import { createRouter, createWebHistory } from "vue-router";
import PatientsView from "../views/PatientsView.vue";
import PatientDetailView from "@/views/PatientDetailView.vue";
import AddPatientView from "@/views/AddPatientView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
    path: '/',
    redirect: '/patients' 
  },
       {
      path: "/patients",
      name: "patients",
      component: PatientsView,
    },
    {
        path: "/patient/:id",
        name: "patient-detail",
        component: PatientDetailView,
    },
    {
      path: "/addPatient",
      name: "add-patient",
      component: AddPatientView,
    }
  ],
});

export default router;
