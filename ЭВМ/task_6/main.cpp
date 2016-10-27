#include <cstdlib>
#include <cstring>
#include <iostream>
#include <sensors/sensors.h>
using namespace std;
int main() {
  int l = 0;
  int k = 0;
  int j = 0;
  double h;
  char* buf = new char[256];
  sensors_chip_name const * chip;
  sensors_feature const * feature;
  sensors_subfeature const * subfeature;
  sensors_cleanup();
  sensors_init(NULL);

  for(;;){
    if((chip = sensors_get_detected_chips(NULL, &l)) != NULL){
      sensors_snprintf_chip_name(buf, 256, chip);
      cout << buf << endl;
      for(;;){
        if((feature = sensors_get_features(chip, &k)) != NULL){
          buf = sensors_get_label(chip, feature);
          cout << buf << endl;
          for(;;){
            if((subfeature = sensors_get_all_subfeatures(chip, feature, &j)) != NULL){
                sensors_get_value(chip, subfeature->number, &h);
                cout << subfeature->number << " " << h << endl;
            }
            else {
              j = 0;
              break;
            }
          }
        }
        else {
          k = 0;
          break;
        }
      }
    }
    else break;
  }

  delete[] buf;

  return 0;
}
