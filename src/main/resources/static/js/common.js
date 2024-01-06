const user = {
  login: (id, pass) => {},
  logout: (callback, error) => {
    $.ajax({
      type: "post",
      url: "/logout",
      success: callback,
      error() {
        console.log("실패");
      },
    });
  },
};

window.fmovie = { user };
