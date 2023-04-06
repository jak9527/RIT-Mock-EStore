import { Product } from './product';
import { Bid } from './bid';
export interface Auction {
    id: number;
    endTime: string;
    product: Product;
    quantity: number;
    maxBid: Bid;
  }