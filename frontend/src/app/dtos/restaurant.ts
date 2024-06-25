export interface RestaurantDto {
  name: string;
  address: string;
}

export interface RestaurantOpeningHoursDto {
  monday: Date[];
  tuesday: Date[];
  wednesday: Date[];
  thursday: Date[];
  friday: Date[];
  saturday: Date[];
  sunday: Date[];
}
