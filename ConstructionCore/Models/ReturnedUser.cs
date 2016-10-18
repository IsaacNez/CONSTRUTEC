using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class ReturnedUser
    {
        public int q_id { get; set; }
        public string q_name { get; set; }
        public int q_code { get; set; }
        public List<int> q_role = new List<int>();
    }
}