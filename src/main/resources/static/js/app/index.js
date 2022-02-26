
//굳이 변수의 속성으로 function을 추가하는 이유,
// 브라우저의 스코프는 공용공간으로 쓰이기 때문에 나중에 로딩된 js에서 같은 이름의 init, save등이 존재하면 나중에 로딩된 js가 덮어쓰게 된다.
// 그래서 중복된 함수 이름이 자주 발생 할 수 있으므로, 유효범위를 만들어서 사용한다. 다른 js와 겹칠 위험이 사라짐.
// 최신 자바스크립트 버전인 ES6이나, 앵귤라, 리액트, 뷰 등은 이런 기능을 프레임워크 레벨에서 지원하고 있다.
var main = {
    init :  function (){
        var _this = this;
        $('#btn-save').on('click', function (){
            _this.save();
        });
        $('#btn-update').on('click', function (){
            _this.update();
        });
        $('#btn-delete').on('click', function (){
            _this.delete();
        });
    }
    , save : function (){
        var data = {
            title : $('#title').val(),
            author : $('#author').val(),
            content : $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
           alert('글이 등록되었습니다.');
           window.location.href = '/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
    , update : function (){
        var data = {
            title : $('#title').val(),
            content : $('#content').val()
        };

        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function (){
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
    , delete : function (id){
        var id = id == undefined ? $('#id').val() : id;

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8'
        }).done(function (){
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error){
            alert(JSON.stringify(error));
        });
    }
};

main.init();
