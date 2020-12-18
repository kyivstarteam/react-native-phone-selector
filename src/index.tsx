import { NativeModules, Platform } from 'react-native';

export interface PhoneResponse {
  selectedPhone: string;
}

type PhoneSelectorType = {
  invokePhoneSelector(): Promise<PhoneResponse>;
};

const { PhoneSelector: PhoneSelectorModule } = NativeModules;

class PhoneSelector {
  static invokePhoneSelector() {
    if (Platform.OS !== 'android') {
      return 'false';
    }
    return PhoneSelectorModule.invokePhoneSelector();
  }
}

export default PhoneSelector as PhoneSelectorType;
