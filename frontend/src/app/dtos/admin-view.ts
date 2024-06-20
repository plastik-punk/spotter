export interface AdminViewDto{
  areaId: number,
  startTime: Date,
  date: Date
}
export interface PredictionDto{
  predictionText: string,
  areaNames: Array<string>,
  predictions: Array<number>
}
export interface ReservationForeCastDto{
  forecast: Array<number>,
  maxPlace: number,
  days: Array<string>
}
