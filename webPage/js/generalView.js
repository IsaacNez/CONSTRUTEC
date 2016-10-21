var index;
var Pid;
var Uid;
var Sname;



var stageForm = angular.module('generalView',[])
.controller('projectMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
         
     
       
        
        // Get the modal

        var modalProjectMaterial = document.getElementById('projectMaterialModal');
        var projectMaterial = document.getElementById("projectMaterial");
        var span1 = document.getElementById("close1");
    
        projectMaterial.onclick = function() {
            modalProjectMaterial.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span1.onclick = function() {
            modalProjectMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalProjectMaterial) {
                modalProjectMaterial.style.display = "none";
                
            }
        }
        
        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth()+1; //January is 0!
        var yyyy = today.getFullYear();
        // if the day have one digit return like 9 or 8 ect, so add it 0 to return 09 and 08
        if(dd<10) {
            dd='0'+dd
        } 
        // if the same soution about the day
        if(mm<10) {
            mm='0'+mm
        } 

        today = yyyy+'-'+mm+'-'+dd;
        
        $http.get('http://desktop-6upj287:7575/api/Generaluser/get/date/'+today)
        .then( function (response) {
        $scope.projectlist = response.data;
        });
    
    
        $scope.getProjectByMaterial= function (){
            $http.get('http://desktop-6upj287:7575//api/Generaluser/get/date,pr_name/'+today+","+$scope.S_Name)
            .then( function (response) {
            $scope.projectlist = response.data;
            });
        }

       
              
       
}]);





stageForm =  angular.module('generalView')
.controller('commentCtrl', ['$scope', '$http', function ($scope, $http) {
       
     
       
        
        // Get the modal

        var modalcommentMaterial = document.getElementById('commentModal');
        var span4 = document.getElementById("close4");
    
       

        // When the user clicks on <span> (x), close the modal
        span4.onclick = function() {
            modalcommentMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalcommentMaterial) {
                modalcommentMaterial.style.display = "none";
                
            }
        }
        
     function  getId(element) {
                alert(" row " + element.rowIndex);
                index = element.rowIndex;
                Pid = document.getElementById("tableProject").rows[index].cells[0].innerHTML;
                Uid = document.getElementById("tableProject").rows[index].cells[1].innerHTML;
                Sname = document.getElementById("tableProject").rows[index].cells[5].innerHTML;
                alert(Pid);
                alert(Uid);
                alert(Sname);
          $http.get('http://desktop-6upj287:7575//api/dbComment/get/p_id/u_id/s_name/'+Pid+"/"+Uid+"/"+Sname)
                .then( function (response) {    
                  $scope.commentlist = response.data;           
            });
         
    
}

        
    

        $scope.addComment = function () {
            var Comment = { 
                "p_id": Pid,
                "u_id": Uid,
                "s_name": Sname,
                "c_description": $scope.C_Description
                

            }
            console.log(Comment); $http.post('http://desktop-6upj287:7575//api/dbComment/post/',Comment).
            success(function (data, status, headers, config) {
                alert('Comment has been posted');
            }).
            error(function (data, status, headers, config) {
                alert('error adding comment')
            });
        }


       
              
       
}]);
