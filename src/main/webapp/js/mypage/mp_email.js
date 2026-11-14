document.addEventListener("DOMContentLoaded", function() 
{
    const domainSelect = document.getElementById("domainSelect");
    const emailDomain = document.getElementById("emailDomain");

    domainSelect.addEventListener("change", function() 
    {
        if (this.value === "direct") 
        {
            emailDomain.value = "";             
            emailDomain.readOnly = false;       
            emailDomain.focus();                
        } 
        else 
        {
            emailDomain.value = this.value;     
            emailDomain.readOnly = true;        
        }
    });
});

$(function() {
	/* 선택자 변수 */
	const emailForm = $("#emailForm");
	const ipEmailid = $("#emailid");
	const ipEmailDomain = $("#emailDomain");
	const sendBtn = $("#sendBtn");
	const ipAuthCode = $("#emailcodemp");
	const emailSubmitBtn = $("#emailSubmitBtn");
	
	/* 유효성 검사 */
	function emailVal() {
		const idVal = ipEmailid.val().trim();
		const domainVal = ipEmailDomain.val().trim();
		
		if (idVal === "") {
			alert("이메일 아이디를 입력해주세요");
			ipEmailid.focus();
			return false;
		}
		
		if (domainVal === "") {
			alert("이메일 도메인을 입력해주세요");
			ipEmailDomain.focus();
			return false;
		}
		
		return true;
	}
	
	/* 페이지 최초 입장 시 */
	ipEmailid.focus();
	
	/* 인증번호 발송 클릭 시 */
	sendBtn.on("click", function() {
		if (!emailVal()) return;
		
		$.ajax({
			url : contextPath + "/sendemail",
			type : "POST",
			data : {
				emailid : ipEmailid.val().trim(),
				emaildomain : ipEmailDomain.val().trim(),
				emailtype : "이메일변경"
			},
			dataType : "json",
			success : function(data) {
				if (data.msg === "OK") {
					alert("입력하신 이메일로 인증번호가 전송되었습니다.");
					ipAuthCode.focus();
				} else {
					alert("인증번호 전송에 실패했습니다. 이메일을 다시 확인해주세요.");
				}
			},
			error : function(err) {
				console.error(err);
				alert("에러 발생");
			}
		});
	});
	
	emailSubmitBtn.on("click", function(e) {
		e.preventDefault(); // submit 방지용
		
		const codeVal = ipAuthCode.val().trim();
		
		if (!emailVal()) return;
		
		if (codeVal === "") {
			alert("인증번호를 입력해주세요.");
			ipAuthCode.focus();
			return;
		}
		
		const currentEmail = $(".form-email-wrap").find(".fs-5").text().replace(/\s+/g, "");
		
		const inputEmail = ipEmailid.val().trim() + "@" + ipEmailDomain.val().trim();
		
		$.ajax({
			url : contextPath + "/codecheck",
			type : "POST",
			data : {
				emailid : ipEmailid.val().trim(),
				emaildomain : ipEmailDomain.val().trim(),
				emailcodemp : codeVal,
				emailtype : "이메일변경"
				
			},
			dataType : "json",
			success : function(data) {
				if (data.msg === "OK") {
					if (inputEmail === currentEmail) {
						window.location.href = contextPath + "/mp_email?status=fail"; 
					} else {
						emailForm[0].submit();
					}
				} else {
					alert("인증번호가 일치하지 않거나 만료되었습니다. 다시 확인해주세요.");
				}
			},
			error : function(err) {
				console.error(err);
				alert("에러 발생");
			}
		});
	});
	
	const urlParams = new URLSearchParams(window.location.search);
	const status  = urlParams.get("status");
	if (status === "success") {
		alert("인증 성공! 이메일 변경이 완료되었습니다.");
		window.history.replaceState({}, document.title, window.location.pathname);
	} else if (status === "duplicate") {
		alert("이미 다른 회원이 사용 중인 이메일입니다. 다른 이메일을 입력해 주세요.");
		window.history.replaceState({}, document.title, window.location.pathname);
	} else if (status === "fail") {
		alert("이메일 변경에 실패했습니다.\n\n" +
		      "1. 이미 다른 회원이 사용 중인 이메일이거나\n" +
		      "2. 현재 사용 중인 이메일과 동일하거나\n" +
		      "3. 로그인 세션 등 요청 정보가 만료되었을 수 있습니다.\n\n" +
		      "입력값을 확인하시거나 다시 로그인 후 시도해 주세요.");
		window.history.replaceState({}, document.title, window.location.pathname);
	}
});