using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class projstage
    {
        public string s_name { get; set; }
        public string s_status { get; set; }
        public DateTime s_datestart { get; set; }
        public DateTime s_dateend { get; set; }
        public int gpd_budget { get; set; }
        public List<prodstage> products = new List<prodstage>(); 
    }
}