$(document).ready(() => {
  console.log("login");

  $(".loginForm1").on("submit", (e) => {
    e.preventDefault();
    console.log(e);
    let id = $("#id").val();
    let pass = $("#pass").val();
    console.log(id, pass);
    if (id.length > 0 && pass.length > 0) {
      // 둘 다 값이 잘 입력된 경우
      $.ajax({
        type: "post",
        url: "/login",
        data: { id, pass },
        success(res) {
          console.log("로그인 성공", res);
          if (res.success) {
            location.reload();
          } else {
            alert("로그인 실패");
          }
        },
        error() {
          console.log("에러");
        },
      });
    }
  })
  .on('click','#join',(e) => {
	console.log(e);
	location.href = "/join";
  })
  ;
});

/*
  {success: true, data: {user_id: '...', user_pass: '...' }}

  {success: false, cause: 'DDDDD', error: e}
*/
