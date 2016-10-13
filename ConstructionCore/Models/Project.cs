using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class Project
    {
        public int p_id { get; set; }
        public string p_location { get; set; }
        public string p_name { get; set; }
        public int p_budget { get; set; }
        public int u_code { get; set; }
        public int u_id { get; set; }

    }
}