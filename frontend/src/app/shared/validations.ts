export const validationPatterns = {
  nonEmpty: /.*\S.*/,
  email: '^(([^<>()\\[\\]\\\\.,;:\\s@"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@"]+)*)|(".+"))@((\\[[0-9]{1,3}\\' +
    '.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,254}))$',
  name: /^[/a-zA-Z]+(([',.`" -/\S+/])*){0,60}$/,
  password: /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d ~!@#$%^&*()_,.-:]{8,}$/,
  companyName: /.*\S.*/,
  onlyNumbers: /^[0-9]*$/,
  contractOrNumbers: /^[a-zA-Z0-9\-\,&][a-zA-Z0-9\-\,&]{0,}$/,
  // Letters (a-z) (A-Z) Numbers (0-9) Some special symbols: ‘ “ < > . # $ ^ * + - _ & space
  nameRegEx: /^[a-zA-Z0-9\s\'\"\<\>\.\#\$\^\*\+\-\_&][a-zA-Z0-9\s\'\"\<\>\.\#\$\^\*\+\-\_&]{0,}$/,
  dateActivivty: /(\d{1,2})\/(\d{1,2})\/(\d{5})/,
  dateFormat: /^[0-9]?[0-9]{1,2}\/[0-9]?[0-9]{1,2}\/(?:[0-9]{4})$/,

  zipCode: /^\d{5}(?:[-]\d{4})?$/,
  phone: /^([+])?(1\s?)?[0-9]{8,16}$/,
  number: /^[0-9]+(\.[0-9]*){0,1}$/,
  notes: /^(?:.|\n){0,2499}$/,
  divisionName: /^[a-zA-Z0-9\'\"\<\>\.\#\$\^\*\+\-\_&][a-zA-Z0-9\s\'\"\<\>\.\#\$\^\*\+\-\_&]{1,29}$/,

  // Letters (a-z) (A-Z) Special symbols: - ‘ . space
  city: /^[a-zA-Z\s\'\.\"\-][a-zA-Z\s\'\.\"\-]{0,}$/,

  registerName: '^[a-zA-Z\\s\\\'\\-][a-zA-Z\\s\\\'\\-]{0,30}$',

  // Letters (a-z) (A-Z) Numbers (0-9) Special symbols: # - ‘ / ( ) . space
  address: /^[a-zA-Z0-9\s\#\'\-\.\/\(\)][a-zA-Z0-9\s\#\'\-\.\/\(\)]{2,250}$/,

  // Letters (a-z) (A-Z) Numbers (0-9) Special symbols: # / ? = % : . - _ &
  website: /^[a-zA-Z0-9\#\/\(\)\?\=\%\:\.\-\_\&][a-zA-Z0-9\#\/\(\)\?\=\%\:\.\-\_\&]{0,}$/,

  // Letters (a-z) (A-Z) Numbers (0-9) Special symbols: - ‘ “ / * & ,
  title: /^[a-zA-Z0-9\/\-\'\"\/\*\&\s][a-zA-Z0-9\/\-\'\"\/\*\&\s]{0,}$/,

  zipCodeFull: /^(?=[0-9]*$){5,}$/, // if allnumbers,

  // Letters (a-z) (A-Z) Numbers (0-9) Special symbols: ‘ - “ space
  locationName: /^[a-zA-Z0-9\s\'\-\"][a-zA-Z0-9\s\'\-\")]{1,}$/,

  // Letters (a-z) (A-Z) Numbers (0-9) Special symbols: ‘ - “ / , space
  equipmentName: /^[a-zA-Z0-9\s\'\-\"\/\,][a-zA-Z0-9\s\'\-\"\/\,]{0,}$/,

  numberWithTwoDigitsAfterPoint: /^\d+(\.\d{1,2})?$/,
  addSettings: /^[a-zA-Z0-9\s\#\:\№\$\,\%\&\*\-\'\"\/][a-zA-Z0-9\s\s\#\:\№\$\,\%\&\*\-\'\"\/]{1,29}$/,
  milestoneName: /^[a-zA-Z0-9\s\'\"\-][a-zA-Z0-9\s\'\"\-]{1,49}$/,
  milestoneProbability: /^[1-9][0-9]?$|^99$/,
  // tslint:disable-next-line:max-line-length
  milestoneDescr: /^[a-zA-Z0-9\s\!\@\#\$\%\^\&\*\(\)\-\+\_\=\[\]\{\}\:\;\'\"\<\>\?\/\,\.][a-zA-Z0-9\s\!\@\#\$\%\^\&\*\(\)\-\+\_\=\[\]\{\}\:\;\'\"\<\>\?\/\,\.]{1,199}$/,
  positiveNumberFloat: /^\d*(\.\d+)?$/,
  equipType: /^[a-zA-Z0-9\s\'\"\-][a-zA-Z0-9\s\'\"\-]{1,49}$/,
  // tslint:disable-next-line:max-line-length
  emailRegex: /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
  firstLastName: /^[a-zA-Z0-9\s\'\"\-][a-zA-Z0-9\s\'\"\-]{0,29}$/,
  tenantName: '^[a-zA-Z\s\'\,][a-zA-Z0-9\s\'\,]{0,}$',
  titleContact: /^[a-zA-Z0-9\s\-\'\"\/\*\&\,][a-zA-Z0-9\s\-\'\"\/\*\&\,]{1,}$/,

  // Letters (a-z), (A-Z) Special symbols: - ‘ space
  contactFullName: /^[a-zA-Z\s\-\'][a-zA-Z\s\-\']{0,}$/,

  // Letters (a-z), (A-Z) Numbers (0-9)  Special symbols: - / ,
  proposalNumber: /^[a-zA-Z0-9\-\/\,][a-zA-Z0-9\-\/\,]{0,}$/,

  link: /(http(s)?:\/\/.)(www\.)?[-a-zA-Z0-9@:%_\+.~#?&=]{2,256}/,

  price: /^\d+(\.\d+)*$/,
};
