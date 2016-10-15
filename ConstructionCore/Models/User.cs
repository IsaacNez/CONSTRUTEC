using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class User
    {
        public int u_id { get; set; }
        public string u_name { get; set; }
        public string u_lname { get; set; }
        public int u_phone { get; set; }
        public string u_password { get; set; }
        public string u_charge { get; set; }

        public int u_code { get; set; }
        public int r_id { get; set; }


    }
}