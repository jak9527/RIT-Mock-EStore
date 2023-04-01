import { Product } from './product';
export interface Auction {
    id: number;
    endTime: string;
    product: Product;
    quantity: number;
    bid: number;
  }