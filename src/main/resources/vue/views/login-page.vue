<template id="login-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Login</h5>
          <div class="card-body">
            <form @submit.prevent="login">
              <div class="form-group">
                <label for="email">Email</label>
                <input type="email" class="form-control" id="email" v-model="email">
              </div>
              <div class="form-group">
                <label for="password">Password</label>
                <input type="password" class="form-control" id="password" v-model="password">
              </div>
              <button type="submit" class="btn btn-primary">Log in</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>
<script>
app.component('login-page',
    {
      template: "#login-page",
      data: () => ({
        email: '',
        password: ''
      }),
      methods: {
        login() {
          axios.post("/api/admins/login", { email: this.email, password: this.password })
              .then(res => {
                // handle successful login, such as storing the returned token in session storage
                localStorage.setItem('token', res.data.token);
                window.location.href = "/";
                this.isLoggedIn = true;
              })
              .catch((e) => {
                // handle failed login
                if (e.response.status === 401) {
                  alert("Invalid email or password");
                } else {
                  alert("Error while logging in");
                }
              });
        }
      }
    });
</script>