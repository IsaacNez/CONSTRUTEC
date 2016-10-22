using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ConstructionCore.Models
{
    public class generaluser
    {
        public int gcs_pid { get; set; }
        public int gcs_uid { get; set; }
        public string gcs_status { get; set; }
        public string gcs_uname { get; set; }
        public int gcs_uphone { get; set; }
        public string gcs_plocation { get; set; }
        public DateTime gcs_datestart { get; set; }
        public string gcs_sname { get; set; }
    }
}