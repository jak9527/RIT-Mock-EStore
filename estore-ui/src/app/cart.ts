import { Product } from "./product";

export interface Cart {
    id: number;
    products: Map<number, Product>;
  }