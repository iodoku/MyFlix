function getByteLength(str)
{
	return new Blob([str]).size;
}

// 수정창 열릴 때 초기 바이트 계산 및 이전값 저장 변수
const editPrevMap = {};

function openCommentEdit(id)
{
    document.getElementById('comment-' + id).classList.add('is-editing');
    
    // 🎯 수정창 열릴 때 기존 댓글 내용의 바이트 수 즉시 반영
    const textarea = $("#comment-edit-textarea-" + id);
    const val = textarea.val();
    const len = getByteLength(val);
    
    editPrevMap[id] = { val: val, len: len };
    
    $("#currentByte-" + id).text(len);
    $("#byteWarning-" + id).hide();
    
    if (len >= 4000) {
        $("#currentByte-" + id).css("color", "#dc3545");
    } else {
        $("#currentByte-" + id).css("color", "#6c757d");
    }
}
  
function cancelCommentEdit(id)
{
    document.getElementById('comment-' + id).classList.remove('is-editing');
}
  
function saveCommentEdit(rp_no)	// 댓글 수정 작성하기 누르면 실행
{
    // 수정된 댓글 내용
    const rp_text = document.getElementById("comment-edit-textarea-" + rp_no).value.trim();
    // 수정된 평점
    const rp_rating = parseFloat(document.getElementById("edit-rating-" + rp_no).textContent);

    // 내용 검사
    if (rp_text === "") 
    {
		alert("댓글 내용을 입력해주세요.");
        return;
    }

    // 🎯 바이트 유효성 검사 추가
    if (getByteLength(rp_text) > 4000)
    {
        alert("댓글 내용은 최대 4000바이트까지만 등록 가능합니다.");
        return;
    }

    $.ajax({
        url: contextPath + "/updatecomment",
        type: "POST",
        data: 
        {
            rp_no: rp_no,				// 댓글 번호
            rp_content: rp_text,		// 댓글 내용
            rp_rating: rp_rating		// 댓글 평점
        },
        success: function(response) 
        {
            location.reload();
        },
        error: function(xhr) 
        {
            alert("댓글 수정 중 오류가 발생했습니다.");
        }
    });
}
  
function deleteComment(rp_no) 	// 댓글 삭제 누르면 실행
{
    if (!confirm("댓글을 삭제하시겠습니까?")) {return;}

    $.ajax({
        url: contextPath + "/deletecomment",
        type: "POST",
        data: {
            rp_no: rp_no	// 댓글 번호
        },
        success: function(response) 
        {
            response = response.trim();

            if (response === "success") 
            {
                alert("댓글이 삭제되었습니다.");
                location.reload();	// 삭제 후 댓글 목록 다시 불러오기
            }
            else if (response === "login") 
            {
                alert("로그인이 필요합니다.");
                location.href = contextPath + "/login";
            }
            else if (response === "fail") 
            {
                alert("댓글 삭제에 실패했습니다.");
            }
            else 
            {
                alert("댓글 삭제 중 오류가 발생했습니다.");
            }
        },
        error: function(xhr) 
        {
            alert("댓글 삭제 중 오류가 발생했습니다.");
        }
    });
}

function insertComment() // 댓글 작성하기 버튼 누르면 실행
{
    // 리뷰 번호
    const r_no = new URLSearchParams(location.search).get("r_no");
    // 댓글 내용
    const rp_content = document.getElementById("reply-content").value;
    // 댓글 평점
    const rp_rating = parseFloat(document.getElementById("reply-rating").textContent);

    if (rp_content.trim() === "") 
    {
        alert("댓글 내용을 입력해주세요.");
        return;
    }
    
    if (getByteLength(rp_content) > 4000)
    {
		alert("댓글 내용은 최대 4000바이트까지만 등록 가능합니다.");
		return;
	}

    $.ajax({
        url: contextPath + "/insertcomment",
        type: "POST",
        data: 
        {
            r_no: r_no,				// 리뷰 번호	
            rp_content: rp_content,	// 댓글 내용
            rp_rating: rp_rating	// 댓글 평점
        },
        success: function(response) 
        {
			console.log("성공");
            // 댓글 작성 후 무조건 1페이지로 이동
            location.href = contextPath + "/reviewdetail?r_no=" + r_no + "&page=1";
        },
        error: function(xhr) 
        {
			console.log("ㅇㅇㅇㅇ");
            console.log(xhr.responseText);
        }

    });
}


// 댓글 수정 별점 클릭
$(document).on("click", ".edit-star", function () 
{
    // 클릭한 별점
    const rating = parseInt($(this).data("rating"));
    // 현재 댓글 번호
    const rp_no = $(this).parent(".edit-stars").data("rp-no");
    // 점수 변경
    $("#edit-rating-" + rp_no).text(rating.toFixed(1));

    // 선택된 별까지 색상 변경
    $(this)
        .parent(".edit-stars")
        .find(".edit-star")
        .each(function () 
        {
            const starRating = parseInt($(this).data("rating"));
            if (starRating <= rating) 
            {
                $(this).addClass("selected");
            } 
            else 
            {
                $(this).removeClass("selected");
            }
        });
});

// 댓글 작성 별점 클릭
$(document).on("click", ".reply-star", function () 
{
    const rating = parseInt($(this).data("rating"));
    // 점수 표시
    $("#reply-rating").text(rating.toFixed(1));
    // 별 색상 변경
    $(".reply-star").each(function () 
    {
        const starRating = parseInt($(this).data("rating"));
        if (starRating <= rating) 
        {
            $(this).addClass("selected");
        } 
        else 
        {
            $(this).removeClass("selected");
        }
    });
});

// 댓글 평점 보여주는 이벤트
$(".edit-stars").each(function () 
{
    const rating = parseFloat($(this).data("rating"));	// 댓글의 평점 가져옴

    $(this).find(".edit-star").each(function () 
    {
        const starRating = parseInt($(this).data("rating"));

        if (starRating <= rating) 			// (별점수 <= 평점) -> 별 선택처리(노란색 변화)
        {
            $(this).addClass("selected");
        } else 
        {
            $(this).removeClass("selected");
        }

    });

});

// 리뷰 삭제 기능
function deleteReview(r_no) 
{
    if (!confirm("리뷰를 삭제하시겠습니까?")) {return;}

    $.ajax({
        url: contextPath + "/deletereview",
        type: "POST",
        data: {
            r_no: r_no	// 삭제할 리뷰 번호
        },
        success: function(response) 
        {
            if (response.result === 1) 
            {
                alert("리뷰가 삭제되었습니다.");
                location.href = contextPath + "/reviewlist";
            } 
            else 
            {
                alert("리뷰 삭제에 실패했습니다.");
            }
        },
        error: function(xhr) 
        {
            alert("리뷰 삭제 중 오류가 발생했습니다.");
        }
    });
}

$(function() {
	
	// 댓글내용 byte 카운터 및 경고 요소
	const currentByte = $("#currentByte");
	const byteWarning = $("#byteWarning");
	const maxCommentByte = 4000;
	let prevReplyVal = "";
	let prevReplyByte = 0;
	
	currentByte.text(prevReplyByte);
	
	// 댓글 작성 실시간 바이트 검사 및 차단
	$(document).on("input", "#reply-content", function() {
		const currentVal = $(this).val();
		const currentLen = getByteLength(currentVal);
		
		// 4000Byte 초과시 직전 유효한 상태로 되돌리기(차단)
		if (currentLen > maxCommentByte)
		{
			byteWarning.show();
			$(this).val(prevReplyVal);
			currentByte.text(prevReplyByte);
			currentByte.css("color", "#dc3545");
			return;
		}
		
		byteWarning.hide();
		prevReplyVal = currentVal;
		prevReplyByte = currentLen;
		
		currentByte.text(currentLen);
		
		// 4000byte에 도달했을 때의 숫자 색상 처리
		if (currentLen >= maxCommentByte)
		{
			currentByte.css("color", "#dc3545");
		} else {
			currentByte.css("color", "#6c757d");
		}
	});

    // 🎯 댓글 수정 실시간 바이트 검사 및 차단 (속성 선택자 사용)
    $(document).on("input", "textarea[id^='comment-edit-textarea-']", function() {
        const id = this.id.replace("comment-edit-textarea-", "");
        const currentByteElem = $("#currentByte-" + id);
        const warningElem = $("#byteWarning-" + id);
        
        if (!editPrevMap[id]) {
            editPrevMap[id] = { val: "", len: 0 };
        }
        
        const currentVal = $(this).val();
        const currentLen = getByteLength(currentVal);
        
        // 4000Byte 초과시 직전 유효한 상태로 되돌리기(차단)
        if (currentLen > maxCommentByte) {
            warningElem.show();
            $(this).val(editPrevMap[id].val);
            currentByteElem.text(editPrevMap[id].len);
            currentByteElem.css("color", "#dc3545");
            return;
        }
        
        warningElem.hide();
        editPrevMap[id].val = currentVal;
        editPrevMap[id].len = currentLen;
        
        currentByteElem.text(currentLen);
        
        if (currentLen >= maxCommentByte) {
            currentByteElem.css("color", "#dc3545");
        } else {
            currentByteElem.css("color", "#6c757d");
        }
    });
});