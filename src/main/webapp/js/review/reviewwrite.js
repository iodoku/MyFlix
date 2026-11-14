$(function(){
	/*** 1. [DOM] 요소 ***/
	// 1-1. 사용자 입력값
	const rtitle = $("#r_title");
	const mtitle = $("#m_title");
	const mdt = $("#m_dt");
	const mgenre1 = $("#m_genre1");
	const mgenre2 = $("#m_genre2");
	const mgenre3 = $("#m_genre3");
	const genreSelects = [mgenre1, mgenre2, mgenre3];
	const rcontent = $("#r_content");
	
	// 1-2. 리뷰내용 byte 카운터 및 경고 요소
	const currentByte = $("#currentByte");
	const byteWarning = $("#byteWarning");
	const maxContentByte = 4000;
	
	// 1-3. submit시 유효성검사용 form id
	const writeForm = $("#writeForm");
	    
	// 1-4. 이미지 업로드
	const imageUpload = $("#imageUpload");	    
	const previewImg = $("#previewImg");
	const uploadPlaceholder = $(".upload-placeholder");
	const initialImgSrc = previewImg.attr("src"); // 수정 모드 기존 이미지 경로 보관
	const allowedImgTypes = ["image/jpeg", "image/png", "image/webp", "image/gif"]; // SVG, TIFF 제외
	const maxImgSize = 5 * 1024 * 1024; // 최대 5MB 제한	
	
	// 1-5. 뒤로가기버튼
	const backBtn = $("#backBtn");
	
	/*** 2. [Function] ***/
	// 2-1. 리뷰제목 유효성 검사
	function rtitleVal(){
		if(rtitle.val().trim() === "")
		{
			alert("리뷰제목을 입력하세요");
			rtitle.focus();
			return false;
		}
		return true;
	}
	
	// 2-2. 영화제목 유효성 검사
	function mtitleVal(){
		if(mtitle.val().trim() === "")
		{
			alert("영화제목을 입력하세요");
			mtitle.focus();
			return false;
		}
		return true;
	}
	
	// 2-3. 영화개봉일자 유효성 검사
	function mdtVal(){
		const mdate = mdt.val().trim();
		const dtRegex = /^[0-9]{4}\.(0[1-9]|1[0-2])\.(0[1-9]|[12][0-9]|3[01])$/;
		// 형식 검사: YYYY.MM.DD (점 구분자 기준 4자리.2자리.2자리)
		if(!dtRegex.test(mdate))
		{
			alert("개봉일자를 올바른 형식으로 입력해 주세요");
			mdt.focus();
			return false;
		}
		
		// 실제 달력상 존재하는 날짜인지 검증
		const parts = mdate.split(".");
		const year = parseInt(parts[0], 10); // 10진수로 변환
		const month = parseInt(parts[1], 10);
		const day = parseInt(parts[2], 10);
		const dateObj = new Date(year, month -1 , day);
		if(dateObj.getFullYear() !== year || dateObj.getMonth() + 1 !== month || dateObj.getDate() !== day)
		{
			alert("존재하지 않는 날짜입니다. 다시 확인해주세요");
			mdt.focus();
			return false;
		}
		return true;
	}
	
	// 2-4. 장르선택 변경시 중복 숨김 및 비활성화
	function updateGenreOption(){
		for(let i = 0; i < genreSelects.length; i++)
		{
			let currentSelect = genreSelects[i];
			currentSelect.find("option").each(function(){
				let optionVal = $(this).val();
			
				if(optionVal === "선택안함" || optionVal === "")
				{
					$(this).prop({ disabled: false, hidden: false });
					return;
				}
				// 다른 select 박스에서 이 optionVal을 선택했는지 확인, 중복 true
				let isAlreadySelected = false;
				for(let j = 0; j < genreSelects.length; j++)
				{
					if(i !== j)
					{
						let otherVal = genreSelects[j].val();
						if(otherVal === optionVal)
						{
							isAlreadySelected = true;
							break;
						}
					}
				}
				// 다른 select 박스에서 이미 선택되었으면 비활성화, 아니면 활성화
				if(isAlreadySelected)
				{
					$(this).prop({ disabled: true, hidden: true });
				}else{
					$(this).prop({ disabled: false, hidden: false });
				}
			});
		}
	}
	
	// 2-5. 장르 선택 안했을시
	function genreVal(){
		if(mgenre1.val() === "선택안함" && mgenre2.val() === "선택안함" && mgenre3.val() === "선택안함")
		{
			alert("장르를 최소 1개 이상 선택해주세요");
			mgenre1.focus();
			return false;
		}
		return true;
	}
	// 2-6. 리뷰내용 UTF-8 바이트 수 계산 (한글 3바이트, 영문/숫자 1바이트)
	function getByteLength(str){
		return new Blob([str]).size;
	} 
	
	// 2-7. 리뷰 내용 유효성검사
	function rcontentVal(){
		if(rcontent.val().trim() === "")
		{
			alert("리뷰내용을 입력해주세요");
			rcontent.focus();
			return false;
		}
		if(getByteLength(rcontent.val()) > maxContentByte)
		{
			alert("리뷰내용은 최대 4000바이트까지만 등록가능합니다");
			rcontent.focus();
			return false;
		}
		return true;
	}
	// 2-8. 이미지 파일 유효성 검사 함수
	function validateImageFile(file) {
		if (!file) return true;
		// SVG, TIFF 등 제외
		if (!allowedImgTypes.includes(file.type))
		{
			alert("JPG, PNG, WEBP, GIF 형식의 이미지만 업로드 가능합니다.");
			return false;
		}
		// 용량 제한 검사 (5MB)
		if (file.size > maxImgSize)
		{
			alert("포스터 이미지는 최대 5MB 이하만 업로드 가능합니다.");
			return false;
		}
		return true;
	}	
	// 2-9. 이미지 UI 상태 복구 함수
    function restoreImageUI() {
        if (initialImgSrc && initialImgSrc.trim() !== "")
        {
            previewImg.attr("src", initialImgSrc).removeClass("d-none");
            uploadPlaceholder.addClass("d-none");
        } else {
            previewImg.attr("src", "").addClass("d-none");
            uploadPlaceholder.removeClass("d-none");
        }
    }	
	
	
	
	
	
	/*** 3. [UI, UX] ***/
	// 3-1. [페이지 로드시]
	// 3-1-1. [수정모드로 들어왔을때 기존 선택된 장르들에 대해 중복 비활성화 적용]
	updateGenreOption();
	
	// 3-1-2. [수정모드로 들어왔을때 기존 리뷰내용 바이트계산 표출]
	let prevContentVal = rcontent.val();
	let prevByteLen = getByteLength(prevContentVal);
	currentByte.text(prevByteLen);
	if(prevByteLen >= maxContentByte)
	{
		currentByte.addClass("text-danger");
	}
	
	// 3-1-3.
	rtitle.focus();
	
	// 3-1-4. [서버응답 메세지 처리]
	//reviewcontroller 에서 msg=fail
	const urlParams = new URLSearchParams(window.location.search);
	const msg = urlParams.get("msg");
	if(msg)
	{
		switch(msg)
		{
			case "fail"	:
				if(urlParams.has("r_no"))
				{
					alert("리뷰 수정에 실패했습니다. 다시 시도해 주세요");
				}else{
					alert("리뷰등록에 실패했습니다. 다시 시도해 주세요");
				} 
				break;
		}
		// msg 파라미터만 제거
		urlParams.delete("msg");
		// 남은 파라미터 (?r_no = 123 등)가 있으면 붙임
		const remainQuery = urlParams.toString();
		const newUrl = window.location.pathname + (remainQuery ? "?" + remainQuery : "");
		
		history.replaceState({}, document.title, newUrl);
	}
	
	/*** 3-2. [이벤트] ***/	
	// 3-2-1 . 영화개봉일자 이벤트
	// 입력중일때
	mdt.on("input", function(){
		let ipval = $(this).val().replace(/[^0-9]/g, "").slice(0, 8); //숫자이외 제거 + 숫자 8자리까지
		$(this).val(ipval);
	});
	// focus 벗어낫을때
	mdt.on("blur", function(){
		let blurval = $(this).val().replace(/[^0-9]/g, "");
		if(blurval.length >= 7) 
		{
        	$(this).val(blurval.replace(/(\d{4})(\d{2})(\d{2})/, "$1.$2.$3"));
    	}else if(blurval.length >= 5 )
    	{
			$(this).val(blurval.replace(/(\d{4})(\d{1,2})/, "$1.$2"));	
		}else if(blurval.length === 4)
		{
			$(this).val(blurval + ".");
		}else
		{
			$(this).val(blurval);
		}
	});
	// focus 할때
	mdt.on("focus", function(){
		let focusval = $(this).val().replace(/[^0-9]/g, "");
		$(this).val(focusval);
	})
	
    // 3-2-2. 포스터 이미지 미리보기 이벤트 + 수정모드시
imageUpload.on("change", function () {
        const file = this.files && this.files[0];

        if (file) {
            // 유효성 검사 실패 시 값 초기화 후 직전 상태로 복원
            if (!validateImageFile(file)) {
                $(this).val("");
                restoreImageUI();
                return;
            }

            const reader = new FileReader();
            reader.onload = function (e) {
                previewImg.attr("src", e.target.result).removeClass("d-none");
                uploadPlaceholder.addClass("d-none");
            };
            reader.readAsDataURL(file);
        } else {
            // 파일 선택 취소 시 기존 상태 복원
            restoreImageUI();
        }
    });

	// 3-2-3. 영화 장르 클릭 이벤트
	mgenre1.on("change", updateGenreOption);
	mgenre2.on("change", updateGenreOption);
	mgenre3.on("change", updateGenreOption);
	
	// 3-2-4. 리뷰내용 실시간 바이트 체크 이벤트
	rcontent.on("input", function(){
		const currentVal = $(this).val();
		const currentLen = getByteLength(currentVal);
		
		// 4000 Byte를 초과하면 직전 유효한 텍스트로 즉각 롤백(차단)
		if(currentLen > maxContentByte)
		{
			byteWarning.show();
			$(this).val(prevContentVal);
			currentByte.text(prevByteLen);
			currentByte.addClass("text-danger");
			return;
		}
		
		// 4000 Byte 이내 정상 입력 시 갱신
		byteWarning.hide();
		prevContentVal = currentVal;
		prevByteLen = currentLen;
		
		currentByte.text(currentLen);
		if(currentLen >= maxContentByte)
		{
			currentByte.addClass("text-danger");
		}else{
			currentByte.removeClass("text-danger");
		}
	});
	
	
	
    // 3-2-5. 뒤로가기 버튼 클릭 이벤트
    backBtn.on('click', function () {
		if(confirm("입력한 내용이 삭제됩니다. 진행하시겠습니까?"))
		{
			history.back();
		}
    });
	
	// 3-2-6. submit 전 유효성 검사
	writeForm.on("submit", function(e){
		if(!rtitleVal())   { e.preventDefault(); return; }
		if(!mtitleVal())   { e.preventDefault(); return; }
		if(!mdtVal())      { e.preventDefault(); return; }
		if(!genreVal())    { e.preventDefault(); return; }
		if(!rcontentVal()) { e.preventDefault(); return; }
		const uploadedFile = imageUpload[0].files && imageUpload[0].files[0];
		if (uploadedFile && !validateImageFile(uploadedFile))
		{
			e.preventDefault();
			return;
		}		
	});
})