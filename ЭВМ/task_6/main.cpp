#include <iostream>
#include <sensors/sensors.h>
using namespace std;
int main() {
    // sensors_chip_name const * cn;
    // int c = 0;
    // while ((cn = sensors_get_detected_chips(0, &c)) != 0) {
    //     std::cout << "Chip: " << cn->prefix << "/" << cn->path << std::endl;
    //
    //     sensors_feature const *feat;
    //     int f = 0;
    //
    //     while ((feat = sensors_get_features(cn, &f)) != 0) {
    //         std::cout << f << ": " << feat->name << std::endl;
    //
    //         sensors_subfeature const *subf;
    //         int s = 0;
    //
    //         while ((subf = sensors_get_all_subfeatures(cn, feat, &s)) != 0) {
    //             std::cout << f << ":" << s << ":" << subf->name
    //                       << "/" << subf->number << " = ";
    //             double val;
    //             if (subf->flags & SENSORS_MODE_R) {
    //                 int rc = sensors_get_value(cn, subf->number, &val);
    //                 if (rc < 0) {
    //                     std::cout << "err: " << rc;
    //                 } else {
    //                     std::cout << val;
    //                 }
    //             }
    //             std::cout << std::endl;
    //         }
    //     }
    // }
  int l = 0;
  char buf[256];
  cout << sensors_get_detected_chips(NULL, &l) << endl;
  // sensors_snprintf_chip_name(buf, 256, sensors_get_detected_chips(NULL, &l));
  return 0;
}
