var index;
var Pid;
var Uid;
var Sname;
var Status;
var url= 'http://desktop-6upj287:7575';

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
        
        
    
        $http.get(url+'/api/Generaluser/get/date/'+today)
        .then( function (response) {
            console.log(response.data);
        $scope.projectlist = response.data;
        });
    
        $scope.getProjectByMaterial= function (){
            $http.get(url+'/api/Costumerserviceprod/get/date,pr_name/'+today+","+$scope.S_Name)
            .then( function (response) {
                
            $scope.projectlist = response.data;
                console.log($scope.projectlist);
            });
        }
        
         $scope.getComment= function (pid,uid,sname){
            Pid = pid;
            Uid = uid;
           Sname = sname;
  
            console.log(Pid);
             console.log(Uid);
              console.log(Sname);
             
        }
        function changeStatus(element){
            getId(element);
             var status = { 
                "pxs_status": Status
            }
            console.log(status); $http.post(url+'/api/projectxstage/post/',status).
            success(function (data, status, headers, config) {
                alert('status has been changed');
            }).
            error(function (data, status, headers, config) {
                alert('error changing status')
            });
        }

           
       
}]);





stageForm =  angular.module('generalView')
.controller('commentCtrl', ['$scope', '$http', function ($scope, $http) {
       
     
       

        
            
        
        
        $scope.displayComment= function (){
            
             $http.get(url+'/api/Comment/get/p_id,s_name/'+Pid+","+"'"+Sname+"'")
                .then( function (response) {    
                 
                  $scope.commentlist = response.data;
                   console.log($scope.commentlist);
                  
         });
              
        }

        $scope.addComment = function () {
            var Comment = { 
                "p_id": Pid,
                "s_name": Sname,
                "c_description": $scope.C_Description
                

            }
            
            console.log(Comment); 
            $http.post(url+'/api/Comment/post/',Comment).
            success(function (data, status, headers, config) {
                alert('Comment has been posted');
            }).
            error(function (data, status, headers, config) {
                alert('error adding comment')
            });
        }


       
              
       
}]);
