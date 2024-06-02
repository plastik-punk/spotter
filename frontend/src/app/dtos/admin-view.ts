export interface AdminViewDto{
  area: String,
  startTime: Date,
  date: Date
}
export interface PredictionDto{
  prediction: String
}
export interface ReservationForeCastDto{
  forecast: Array<number>,
  maxPlace: number,
  days: Array<string>
}
