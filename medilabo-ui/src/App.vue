<template>
  <header>
    <nav>
      <router-link to="/">Accueil</router-link> |
      <router-link to="/patients">Patients</router-link>
      <router-link to="/addPatient">Ajouter un patient</router-link> 
    </nav>
  </header>

  <main>
    <router-view />
  </main>
</template>

<style>
nav {
  padding: 20px;
  background-color: #ffcdd3;
  font-family: Arial, sans-serif;
}

nav a {
  text-decoration: none; 
  color: #333;           
}

nav a:visited, 
nav a:hover, 
nav a:active {
  text-decoration: none;
  color: #333;
}

</style>
