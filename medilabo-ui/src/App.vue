<template>
  <header>
    <nav>
      <router-link to="/">Accueil</router-link> | 
      <router-link to="/patients">Patients</router-link>
    </nav>
  </header>

  <main>
    <router-view />
  </main>
</template>

<style>
nav {
  padding: 20px;
  background-color: #f4f4f4;
}
</style>