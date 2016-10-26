    
//Var Globals
var index;
var Pid;
var Uid;
var Sname;
var Status;
var userid;
var url= 'http://desktop-6upj287:7575';

/**
* Show all stages of projects that will start in the next 15 days
*/ 
var stageForm = angular.module('generalView',[])
.controller('projectMaterialCtrl', ['$scope', '$http', function ($scope, $http) {
         
     
       
        userid = localStorage.user;// Get the user Id
        console.log(" id " + userid);
        document.getElementById("idUser").innerHTML = "Welcome "+userID;//Show the user name
        // Get the modal

    
        // Get the modals

        var modalProjectMaterial = document.getElementById('projectMaterialModal');
        var modalcommentMaterial = document.getElementById('commentModal');
        
        // Buttons that has the option to open the modal
        var projectMaterial = document.getElementById("projectMaterial");
    
        // Spans to close the modals
        var span1 = document.getElementById("close1");
        var span4 = document.getElementById("close4");

        
        // When the user click on it display the modal
        projectMaterial.onclick = function() {
            
            modalProjectMaterial.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span1.onclick = function() {
            modalProjectMaterial.style.display = "none";
        }
        span4.onclick = function() {
            modalcommentMaterial.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function(event) {
            if (event.target == modalProjectMaterial) {
                modalProjectMaterial.style.display = "none";
                
            }
        }
        
        window.onclick = function(event) {
            if (event.target == modalcommentMaterial) {
                modalcommentMaterial.style.display = "none";
                
            }
        } 
        
        
        var today = new Date();// Take the system date
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
        // Concatenate the year-month-day
        today = yyyy+'-'+mm+'-'+dd;
        
        
        /**
         * Get all projects that will start in the next 15 days from today
         * <p>
         * @return: a list of stages proyect 
         * 
        */
        $http.get(url+'/api/Generaluser/get/date/'+today)
        .then( function (response) {
            console.log(response.data);
        $scope.projectlist = response.data;
        });
        
        /**
         * Get all projects that will start in the next 15 days from today and that has a 
         * specific product
         * <p>
         * @return: a list of stages proyect 
         * 
        */
        $scope.getProjectByMaterial= function (){
            $http.get(url+'/api/Costumerserviceprod/get/date,pr_name/'+today+","+$scope.S_Name)
            .then( function (response) {
                
            $scope.projectlist = response.data;
                console.log($scope.projectlist);
            });
        }
        
        /**
         * Set the values to the var globals
         * <p>
         * @param: pid - Id proyect
         * @param: uid - Id user
         * @param: sname - Name of the stage
        */
         $scope.getComment= function (pid,uid,sname){
            Pid = pid;
            Uid = uid;
           Sname = sname;
  
            console.log(Pid);
             console.log(Uid);
              console.log(Sname);
             
        }
         
        
        /**
         * Change the stage status to ready
         * <p>
         * @param: s - Item from the specific row where is editing 
         * @param: uid - Id user
         * @param: sname - Name of the stage
        */
        $scope.updateStatus= function (s,pid,sname){
             s.gcs_status = "Ready";
            console.log(pid);
            
              console.log(sname);
           $http.get(url+'/api/Projectxstage/update/pxs_status,p_id,s_name/'+"'"+"Hola"+"'"+","+pid+","+"'"+sname+"'").
            success(function (data, status, headers, config) {
                alert('status has been change');
            }).
            error(function (data, status, headers, config) {
                alert('error changing status')
            });
              
        }
           
       
}]);


/**
* Show all comments from a specific stage
*/
stageForm =  angular.module('generalView')
.controller('commentCtrl', ['$scope', '$http', function ($scope, $http) {
         
        
        /**
         * Display or show all comments
         * <p>
         * @return: list of all comments
         * 
        */
        $scope.displayComment= function (){
            
             $http.get(url+'/api/Comment/get/p_id,s_name/'+Pid+","+"'"+Sname+"'")
                .then( function (response) {    
                 
                  $scope.commentlist = response.data;
                   console.log($scope.commentlist);
                  
         });
              
        }

        /**
         * Post the comment that the user wrote
         * <p>
        */
        $scope.addComment = function () {
            var Comment = { 
                "p_id": Pid,
                "u_id":userid,
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
