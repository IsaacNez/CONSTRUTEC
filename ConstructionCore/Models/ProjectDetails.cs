using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class ProjectDetails
    {
        public int gpd_id { get; set; }
        public string gpd_name { get; set; }
        public string gpd_location { get; set; }
        public int gdp_pbudget { get; set; }
        
        public int gpd_engineer { get; set; }
        public int gpd_owner { get; set; }
        public List<projstage> stages = new List<projstage>();
    }
}