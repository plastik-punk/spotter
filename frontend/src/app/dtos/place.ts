import {StatusEnum} from "./status-enum";

export interface Place {
  id?: number;
  pax: number;
  status: StatusEnum;
}

