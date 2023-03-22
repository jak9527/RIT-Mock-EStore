import { Product } from "./product";

export interface Cart {
    id: number;
    total: number;
    products: Map<number, Product>;
  }