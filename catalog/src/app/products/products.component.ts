import {Component, OnInit} from '@angular/core';
import {ProductService} from "../services/product.service";
import {PageProduct, Product} from "../model/product.model";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent implements OnInit{
  products! : Array<Product> ;
  currentPage : number=0;
  pageSize : number=5;
  totalPages : number=0;
  errorMessage! : String;
  searchFormGroup! : FormGroup;
  currentAction : string="all";
  constructor(private productService : ProductService, private fb :FormBuilder) {}
  ngOnInit(): void {
    this.searchFormGroup=this.fb.group({
      keyword : this.fb.control(null)
    });
    //this.handleGetAllProduct();
    this.handleGetPageProduct();
  }
  handleGetPageProduct(){
    this.productService.getPageProducts(this.currentPage,this.pageSize).subscribe({
      next : (data ) =>{
        this.products=data.products;
        this.totalPages=data.totalPages;
      },
      error : (err)=>{
        this.errorMessage=err;
      }
    });
  }
  handleGetAllProducts(){
    this.productService.getAllProducts().subscribe({
      next : (data : any)=>{
        this.products=data;
      },
      error : (err)=>{
        this.errorMessage=err;
      }
    });
  }
  /*handleDeleteProduct(p: Product){
    let index=this.products.indexOf(p);
    this.products.splice(index,1);
  }*/
  handleDeleteProduct(p: any) {
    let conf=confirm("Are you sure?");
    if(conf==false) return;
    this.productService.deleteProduct(p.id).subscribe({
      next : (data)=>{
        //this.handleGetAllProducts();
        let index=this.products.indexOf(p);
        this.products.splice(index,1);
      }
    })
  }

  handleSetPromotion(p: Product) {
    let promo=p.promotion;
     this.productService.setPromotion(p.id).subscribe({
       next : (data)=>{
         p.promotion=! promo;
       },
       error : err =>{
         this.errorMessage=err;
       }
     })
  }

  handleSearchProducts() {
    this.currentAction="search";
    this.currentPage=0;
    let keyword=this.searchFormGroup.value.keyword;
    this.productService.searchProducts(keyword,this.currentPage,this.pageSize).subscribe({
      next :(data)=>{
        this.products=data.products;
        this.totalPages=data.totalPages;
      }
    })
  }

  gotoPage(i: number) {
    this.currentPage=i;
    if(this.currentAction ==='all')
      this.handleGetPageProduct();
    else
      this.handleSearchProducts();
  }
}
