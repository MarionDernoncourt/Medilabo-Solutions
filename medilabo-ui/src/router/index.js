import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import PatientsView from "../views/PatientsView.vue";
import PatientDetailView from "@/views/PatientDetailView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
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
  ],
});

export default router;
